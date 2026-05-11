package dev.sauloaraujo.sgb.apresentacao.acervo.livro;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import dev.sauloaraujo.sgb.aplicacao.acervo.livro.LivroResumoExpandido;
import dev.sauloaraujo.sgb.aplicacao.acervo.livro.LivroServicoAplicacao;
import jakarta.annotation.PostConstruct;

@Route("/livro/pesquisar")
public class LivroPesquisarRota extends VerticalLayout implements AfterNavigationObserver {
	private static final long serialVersionUID = -9031401005322963865L;

	private @Autowired LivroServicoAplicacao servico;

	private Grid<LivroResumoExpandido> grade;

	@PostConstruct
	private void configurar() {
		var titulo = new H1("Livros");
		add(titulo);

		grade = new Grid<>(LivroResumoExpandido.class, false);
		grade.addComponentColumn(this::link).setHeader("ISBN");
		grade.addColumn("livro.titulo").setHeader("Título");
		grade.addColumn("autor.nome").setHeader("Autor");
		grade.addColumn("exemplaresDisponiveis").setHeader("Exemplares disponíveis");
		grade.addColumn("totalExemplares").setHeader("Total de exemplares");
		add(grade);
	}

	private RouterLink link(LivroResumoExpandido resumo) {
		var id = resumo.getLivro().getId();
		return new RouterLink(id, LivroEdicaoRota.class, id);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		var livros = servico.pesquisarResumosExpandidos();
		grade.setItems(livros);
	}
}