import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Transaction, Account, FinancialSummary } from '../models/financial.model';

@Injectable({
  providedIn: 'root'
})
export class FinancialService {
  private accounts: Account[] = [
    { id: 1, name: 'Main Checking', type: 'Checking', balance: 12500.00, currency: 'USD' },
    { id: 2, name: 'Savings Account', type: 'Savings', balance: 45000.00, currency: 'USD' },
    { id: 3, name: 'Investment Portfolio', type: 'Investment', balance: 78500.00, currency: 'USD' },
    { id: 4, name: 'Credit Card', type: 'Credit', balance: -2350.00, currency: 'USD' }
  ];

  private transactions: Transaction[] = [
    { id: 1, date: '2026-02-28', description: 'Salary Deposit', amount: 8500.00, type: 'income', category: 'Salary' },
    { id: 2, date: '2026-02-27', description: 'Grocery Store', amount: -156.75, type: 'expense', category: 'Food' },
    { id: 3, date: '2026-02-26', description: 'Electric Bill', amount: -125.00, type: 'expense', category: 'Utilities' },
    { id: 4, date: '2026-02-25', description: 'Freelance Payment', amount: 2500.00, type: 'income', category: 'Freelance' },
    { id: 5, date: '2026-02-24', description: 'Internet Subscription', amount: -89.99, type: 'expense', category: 'Subscriptions' },
    { id: 6, date: '2026-02-23', description: 'Gas Station', amount: -65.00, type: 'expense', category: 'Transportation' },
    { id: 7, date: '2026-02-22', description: 'Restaurant', amount: -78.50, type: 'expense', category: 'Food' },
    { id: 8, date: '2026-02-21', description: 'Dividend Income', amount: 350.00, type: 'income', category: 'Investment' }
  ];

  getAccounts(): Observable<Account[]> {
    return of(this.accounts);
  }

  getTransactions(): Observable<Transaction[]> {
    return of(this.transactions);
  }

  getFinancialSummary(): Observable<FinancialSummary> {
    const totalIncome = this.transactions
      .filter(t => t.type === 'income')
      .reduce((sum, t) => sum + t.amount, 0);
    
    const totalExpenses = Math.abs(this.transactions
      .filter(t => t.type === 'expense')
      .reduce((sum, t) => sum + t.amount, 0));
    
    const totalBalance = this.accounts.reduce((sum, a) => sum + a.balance, 0);
    const savingsRate = totalIncome > 0 ? ((totalIncome - totalExpenses) / totalIncome) * 100 : 0;

    return of({
      totalIncome,
      totalExpenses,
      balance: totalBalance,
      savingsRate
    });
  }

  addTransaction(transaction: Omit<Transaction, 'id'>): Observable<Transaction> {
    const newTransaction: Transaction = {
      ...transaction,
      id: this.transactions.length + 1
    };
    this.transactions.unshift(newTransaction);
    return of(newTransaction);
  }
}
