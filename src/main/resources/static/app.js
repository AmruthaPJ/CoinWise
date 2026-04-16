let currentTotal = 0;
const BUDGET_LIMIT = 5000;

document.getElementById('splitType').addEventListener('change', (e) => {
    const helper = document.getElementById('splitHelper');
    const type = e.target.value;
    if (type === 'EQUAL') helper.innerText = "Splits equally among Alice and Bob.";
    if (type === 'PERCENTAGE') helper.innerText = "Splits 60% for Alice and 40% for Bob.";
    if (type === 'EXACT') helper.innerText = "Splits with a custom logic where Alice pays more.";
});

document.getElementById('expenseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const submitBtn = document.getElementById('submitBtn');
    const msgBox = document.getElementById('statusMessage');
    const amount = parseFloat(document.getElementById('amount').value);
    const desc = document.getElementById('expenseName').value;
    const splitType = document.getElementById('splitType').value;
    
    // Disable button to prevent double submit
    submitBtn.disabled = true;
    submitBtn.innerText = "Processing...";
    msgBox.className = 'status-message'; // reset

    // Determine parameters for Exact/Percentage strategies 
    // Usually these come from a complex form, but we hardcode the arrays for the Viva demo
    let params = [];
    if (splitType === 'PERCENTAGE') {
        params = [60.0, 40.0]; // 60% and 40%
    } else if (splitType === 'EXACT') {
        params = [amount * 0.7, amount * 0.3]; // For exactly $70 and $30 if amount is 100
    }

    // Spring Boot JSON Payload mapping to our DTO
    const payload = {
        amount: amount,
        splitType: splitType,
        group: {
            name: "Viva Project Group",
            totalSpent: currentTotal,
            budget: { limit: BUDGET_LIMIT }
        },
        users: [
            { name: "Alice", email: "alice@example.com" },
            { name: "Bob", email: "bob@example.com" }
        ],
        params: params
    };

    try {
        const response = await fetch('/api/v1/expenses/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        const text = await response.text();
        
        if (response.ok) {
            msgBox.classList.add('status-success');
            msgBox.innerHTML = `✅ ${text}`;
            updateDashboard(amount, desc, splitType);
            document.getElementById('expenseForm').reset();
        } else {
            msgBox.classList.add('status-error');
            msgBox.innerHTML = `❌ Error: ${text}`;
        }
    } catch(err) {
        msgBox.classList.add('status-error');
        msgBox.innerHTML = `❌ Connection Error. Is Spring Boot running?`;
    } finally {
        submitBtn.disabled = false;
        submitBtn.innerText = "Split Expense";
    }
});

function updateDashboard(amount, desc, splitType) {
    // Update Total
    currentTotal += amount;
    document.getElementById('totalSpentDisplay').innerText = `₹${currentTotal.toFixed(2)}`;
    
    // Update Budget Alert (Observer Pattern simulation trigger effect visible to user)
    const statusEl = document.getElementById('budgetStatus');
    if (currentTotal > BUDGET_LIMIT) {
        statusEl.innerHTML = `Status: <span class="badge danger">Exceeded</span> (Observers Notified!)`;
    }

    // Add to activity list
    const list = document.getElementById('activityList');
    const emptyState = list.querySelector('.empty-state');
    if (emptyState) emptyState.remove();

    const li = document.createElement('li');
    li.innerHTML = `
        <div class="act-details">
            <strong>${desc}</strong>
            <span>Split: ${splitType}</span>
        </div>
        <div class="act-amount">+ ₹${amount.toFixed(2)}</div>
    `;
    list.prepend(li); // add to top
}
