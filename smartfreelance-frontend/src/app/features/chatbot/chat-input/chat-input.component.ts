import { Component, EventEmitter, Output, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, filter, catchError } from 'rxjs/operators';
import { ChatSuggestionService } from '../../../core/services/chat-suggestion.service';

@Component({
  selector: 'app-chat-input',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-input.component.html',
  styleUrls: ['./chat-input.component.css']
})
export class ChatInputComponent implements OnInit, OnDestroy {
  @Output() messageSent = new EventEmitter<string>();
  @ViewChild('inputField') inputField!: ElementRef;

  userInput: string = '';
  suggestions: string[] = [];
  isLoading: boolean = false;
  showSuggestions: boolean = false;

  private searchSubject = new Subject<string>();
  private subscription: Subscription = new Subscription();

  constructor(private suggestionService: ChatSuggestionService) {}

  ngOnInit(): void {
    this.subscription.add(
      this.searchSubject.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(query => query.length >= 2),
        switchMap(query => {
          this.isLoading = true;
          console.log('Fetching suggestions for:', query);
          return this.suggestionService.getSuggestions(query).pipe(
            catchError(err => {
              console.error('API Error details:', err);
              return of([]);
            })
          );
        })
      ).subscribe({
        next: (res) => {
          console.log('Suggestions received:', res);
          this.suggestions = res;
          this.isLoading = false;
          this.showSuggestions = this.suggestions.length > 0;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onInput(): void {
    if (this.userInput.length < 2) {
      this.suggestions = [];
      this.showSuggestions = false;
      return;
    }
    console.log('User input detected:', this.userInput);
    this.searchSubject.next(this.userInput);
  }

  selectSuggestion(suggestion: string): void {
    console.log('Suggestion clicked:', suggestion);
    this.userInput = suggestion;
    this.showSuggestions = false;
    this.sendMessage();
  }

  sendMessage(): void {
    if (!this.userInput.trim()) return;
    console.log('Emitting message:', this.userInput);
    this.messageSent.emit(this.userInput);
    this.userInput = '';
    this.suggestions = [];
    this.showSuggestions = false;
  }

  highlightMatch(text: string): string {
    if (!this.userInput) return text;
    const re = new RegExp(`(${this.userInput})`, 'gi');
    return text.replace(re, '<strong>$1</strong>');
  }

  onBlur(): void {
    // Petit délai pour permettre le clic sur une suggestion avant de fermer
    setTimeout(() => {
      this.showSuggestions = false;
    }, 200);
  }
}
