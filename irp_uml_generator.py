# create LSTM model class
class LSTM_Model(nn.Module):
    def __init__(self, input_layer, hidden_layer, output_layer, num_layers=4, dropout=0.3):
        super(LSTM_Model, self).__init__()
        self.hidden_layer = hidden_layer
        self.num_layers = num_layers
        
        # LSTM layers with dropout
        self.lstm = nn.LSTM(input_layer, hidden_layer, num_layers, batch_first=True, dropout=dropout)
        
        # Dropout layer
        self.dropout = nn.Dropout(dropout)
        
        # Linear layer
        self.linear_layer = nn.Linear(hidden_layer, output_layer)

    def forward(self, input_tensor):
        # Initialize hidden state and cell state
        h0 = torch.zeros(self.num_layers, input_tensor.size(0), self.hidden_layer).to(input_tensor.device)
        c0 = torch.zeros(self.num_layers, input_tensor.size(0), self.hidden_layer).to(input_tensor.device)
        
        # Pass through LSTM layers
        out, _ = self.lstm(input_tensor, (h0, c0))
        
        # Apply dropout
        out = self.dropout(out[:, -1, :])
        
        # Pass through linear layer
        out = self.linear_layer(out)
        return out

# create sequences for input data and corresponding labels
def create_sequence(input_data, sequence_length):
    sequences = []
    for i in range(0, len(input_data) - sequence_length):
        sequence = input_data[i : i + sequence_length, :-1]
        label = input_data[i + sequence_length, -1]
        sequences.append((sequence, label))
    return sequences

# train the model with data provided
def trainer(model, train_data, loss_func, opt, epochs, fold, losses):
    model.train()
    for epoch in range(epochs):
        for sequence, labels in train_data:
            opt.zero_grad()
            sequence = sequence.clone().detach().float()
            labels = labels.clone().detach().float().view(-1, 1)
            
            y = model(sequence)
            loss = loss_func(y, labels)
            loss.backward()
            opt.step()

        if epoch % 25 == 1:
            print(f'Epoch {epoch} loss: {loss.item():.4f}')

            new_row = pd.DataFrame({'Fold': [fold+1], 'Epoch': [epoch], 'Loss': [loss.item()]})
            losses = pd.concat([losses, new_row], ignore_index=True)
            
    return losses

def predictor(model, test_data):
    model.eval()
    predictions = []
    with torch.no_grad():
        for sequence, _ in test_data:
            sequence = sequence.clone().detach().float()
            y = model(sequence)
            batch_predictions = torch.sigmoid(y)

            # Convert predictions to tensor if they are scalar or float
            if isinstance(batch_predictions, float) or batch_predictions.ndimension() == 0:
                batch_predictions = torch.tensor([batch_predictions])

            batch_predictions = torch.round(batch_predictions)

            predictions.extend(batch_predictions.squeeze().tolist())
    
    return predictions

# create sequences
sequence_length = 10
sequences = create_sequence(ticker[['Close', 'Volume', 'CPI', 'Mortgage_rate', 'disposable_income',
                                    'GVZ', 'OVX', 'VVIX', 'RSI (14D)', '20 Day CCI', 'Williams %R', 
                                    'MA50', 'Target']].values, sequence_length)

# cross-validation
tscv = TimeSeriesSplit(n_splits=10)
accuracies = []
precisions = []
recalls = []
f1s = []
kappas = []
losses = pd.DataFrame(columns=['Fold', 'Epoch', 'Loss'])

for fold, (train_index, test_index) in enumerate(tscv.split(sequences)):
    print(f'Fold {fold+1}')
    
    train_sequences = [sequences[i] for i in train_index]
    test_sequences = [sequences[i] for i in test_index]
    
    train_data = torch.utils.data.DataLoader(train_sequences, shuffle=True, batch_size=32)
    test_data = torch.utils.data.DataLoader(test_sequences, shuffle=False, batch_size=32)
    
    # initialize model
    model = LSTM_Model(input_layer=12, hidden_layer=10, output_layer=1)
    loss_func = nn.BCEWithLogitsLoss()
    opt = optim.Adam(model.parameters(), lr=0.001, weight_decay=0.0001)
    
    # train model
    epochs = 150
    losses = trainer(model, train_data, loss_func, opt, epochs, fold, losses)
    
    # make predictions
    test_labels = [label for _, label in test_sequences]
    predictions = predictor(model, test_data)
    
    # calculate statistics
    accuracy = accuracy_score(test_labels, predictions)
    precision = precision_score(test_labels, predictions, zero_division=0)
    recall = recall_score(test_labels, predictions)
    f1 = f1_score(test_labels, predictions)
    kappa = cohen_kappa_score(test_labels, predictions)
    
    accuracies.append(accuracy)
    precisions.append(precision)
    recalls.append(recall)
    f1s.append(f1)
    kappas.append(kappa)
    
    print(f'\nAccuracy: {accuracy:.4f}')
    print(f'Precision: {precision:.4f}')
    print(f'Recall: {recall:.4f}')
    print(f'F1: {f1:.4f}')
    print(f'Kappa: {kappa:.4f}\n')

# average scores across all folds
avg_accuracy = np.mean(accuracies)
avg_precision = np.mean(precisions)
avg_recall = np.mean(recalls)
avg_f1 = np.mean(f1s)
avg_kappa = np.mean(kappas)

print(f'\nCross-Validation Results:')
print(f'Average Accuracy: {avg_accuracy:.4f}')
print(f'Average Precision: {avg_precision:.4f}')
print(f'Average Recall: {avg_recall:.4f}')
print(f'Average F1: {avg_f1:.4f}')
print(f'Average Kappa: {avg_kappa:.4f}')
