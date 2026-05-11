package dev.sauloaraujo.sgb.apresentacao;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import dev.sauloaraujo.sgb.apresentacao.acervo.livro.LivroFormulario.LivroDto;
import dev.sauloaraujo.sgb.dominio.acervo.autor.AutorId;
import dev.sauloaraujo.sgb.dominio.acervo.exemplar.ExemplarId;
import dev.sauloaraujo.sgb.dominio.acervo.livro.Isbn;
import dev.sauloaraujo.sgb.dominio.acervo.livro.IsbnFabrica;
import dev.sauloaraujo.sgb.dominio.acervo.livro.Livro;
import dev.sauloaraujo.sgb.dominio.administracao.socio.SocioId;

@Component
public class BackendMapeador extends ModelMapper {
	private IsbnFabrica isbnFabrica;

	BackendMapeador() {
		isbnFabrica = new IsbnFabrica();

		addConverter(new AbstractConverter<LivroDto, Livro>() {
			@Override
			protected Livro convert(LivroDto source) {
				var id = map(source.id, Isbn.class);
				List<AutorId> autores = map(source.autores, new TypeToken<List<AutorId>>() {
				}.getType());

				return new Livro(id, source.titulo, source.subTitulo, autores);
			}
		});

		addConverter(new AbstractConverter<Livro, LivroDto>() {
			@Override
			protected LivroDto convert(Livro source) {
				var dto = new LivroDto();
				dto.id = source.getId().toString();
				dto.titulo = source.getTitulo();
				dto.subTitulo = source.getSubTitulo();
				dto.autores = source.getAutores().stream().map(AutorId::getId).collect(toList());
				return dto;
			}
		});

		addConverter(new AbstractConverter<String, Isbn>() {
			@Override
			protected Isbn convert(String source) {
				return isbnFabrica.construir(source);
			}
		});
		
		addConverter(new AbstractConverter<Integer, AutorId>() {
			@Override
			protected AutorId convert(Integer source) {
				return new AutorId(source);
			}
		});		

		addConverter(new AbstractConverter<Integer, SocioId>() {
			@Override
			protected SocioId convert(Integer source) {
				return new SocioId(source);
			}
		});

		addConverter(new AbstractConverter<Integer, ExemplarId>() {
			@Override
			protected ExemplarId convert(Integer source) {
				return new ExemplarId(source);
			}
		});
	}

	@Override
	public <D> D map(Object source, Class<D> destinationType) {
		return source != null ? super.map(source, destinationType) : null;
	}
}