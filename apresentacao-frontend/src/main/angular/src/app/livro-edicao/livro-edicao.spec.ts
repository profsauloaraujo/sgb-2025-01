import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LivroEdicao } from './livro-edicao';

describe('LivroEdicao', () => {
  let component: LivroEdicao;
  let fixture: ComponentFixture<LivroEdicao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LivroEdicao]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LivroEdicao);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
