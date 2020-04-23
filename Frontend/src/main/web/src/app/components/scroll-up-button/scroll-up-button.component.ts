import {Component, HostListener, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from "@angular/common";

@Component({
  selector: 'app-scroll-up-button',
  template: `
    <!-- Scroll to top button -->
    <div [ngClass]="{'show-scrollTop': windowScrolled}" class="scroll-to-top">
      <button (click)="scrollToTop()" aria-label="Scroll to top" color="basic" mat-fab>
        <em class="fa fa-chevron-up"></em>
      </button>
    </div>
  `,
  styleUrls: ['./scroll-up-button.component.css']
})
export class ScrollUpButtonComponent implements OnInit {
  windowScrolled: boolean;

  constructor(@Inject(DOCUMENT) private document: Document) {
  }

  ngOnInit() {
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 100) {
      this.windowScrolled = true;
    } else if (this.windowScrolled && window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop < 10) {
      this.windowScrolled = false;
    }
  }

  scrollToTop() {
    (function smoothscroll() {
      let currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
      if (currentScroll > 0) {
        window.requestAnimationFrame(smoothscroll);
        window.scrollTo(0, currentScroll - (currentScroll / 8));
      }
    })();
  }
}
