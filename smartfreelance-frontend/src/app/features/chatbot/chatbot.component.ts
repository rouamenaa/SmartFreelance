import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatbotService, FreelancerRecommendation } from '../../core/services/chatbot.service';

import { ChatInputComponent } from './chat-input/chat-input.component';

interface Message {
  text: string;
  isUser: boolean;
  recommendations?: FreelancerRecommendation[];
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule, ChatInputComponent],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent {
  messages: Message[] = [
    { text: 'Bonjour ! Comment puis-je vous aider aujourd\'hui ?', isUser: false }
  ];
  isLoading: boolean = false;

  constructor(private chatbotService: ChatbotService) {}

  handleNewMessage(userText: string) {
    console.log('ChatbotComponent received message:', userText);
    this.messages.push({ text: userText, isUser: true });
    this.isLoading = true;

    this.chatbotService.getRecommendations(userText).subscribe({
      next: (recs) => {
        console.log('Recommendations received for suggestion:', recs);
        this.isLoading = false;
        if (recs && recs.length > 0) {
          this.messages.push({
            text: 'Voici les meilleurs freelancers pour votre besoin :',
            isUser: false,
            recommendations: recs
          });
        } else {
          this.messages.push({
            text: 'Désolé, je n\'ai pas trouvé de freelancer correspondant à ces compétences.',
            isUser: false
          });
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.messages.push({
          text: 'Une erreur est survenue lors de la recherche.',
          isUser: false
        });
        console.error(err);
      }
    });
  }
}
