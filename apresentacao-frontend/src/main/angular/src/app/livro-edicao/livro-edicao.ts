import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, ResolveData, Router, RouterStateSnapshot } from '@angular/router';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { Observable, OperatorFunction, debounceTime, distinctUntilChanged, map } from 'rxjs';

@Component({
  selector: 'app-livro-edicao',
  imports: [FormsModule, NgbTypeahead],
  templateUrl: './livro-edicao.html',
  styleUrl: './livro-edicao.css',
})
export class LivroEdicao {
  static readonly ID = 'id'
  static readonly RECURSO = 'recurso'

  recurso: any
  autorSelecionado: any

  constructor(private readonly httpClient: HttpClient,
    private readonly rota: ActivatedRoute,
    private readonly roteador: Router) { }

  ngOnInit() {
    this.recurso = this.rota.snapshot.data[LivroEdicao.RECURSO]
  }

  autor(id: any) {
    return this.recurso.autores.find((autor: any) => autor.id === id)
  }

  excluirAutor(id: any) {
    this.recurso.livro.autores = this.recurso.livro.autores.filter((autorId: any) => autorId !== id)
  }

  autores: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map((term) =>
        term.length === 0 ? [] : this.recurso.autores.filter((autor: any) => autor.nome.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10),
      )
    )

  autorNome = (autor: any) => autor.nome

  adicionarAutor() {
    if (this.autorSelecionado) {
      this.recurso.livro.autores.push(this.autorSelecionado.id)
      this.autorSelecionado = null
    }
  }

  salvar() {
    this.httpClient.post(`/backend/livro/${this.recurso.livro.id}/salvar`, this.recurso.livro).subscribe(() => {
      this.roteador.navigate(['/livro/pesquisa'])
    })
  }
}

export const LIVRO_EDICAO_RESOLVEDORES: ResolveData = {}
LIVRO_EDICAO_RESOLVEDORES[LivroEdicao.RECURSO] = (rota: ActivatedRouteSnapshot, estado: RouterStateSnapshot) => {
  const id = rota.params[LivroEdicao.ID]
  return inject(HttpClient).get(`/backend/livro/${id}/edicao`)
}