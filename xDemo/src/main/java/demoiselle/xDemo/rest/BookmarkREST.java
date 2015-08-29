package demoiselle.xDemo.rest;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import br.gov.frameworkdemoiselle.BadRequestException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import demoiselle.xDemo.business.BookmarkBC;
import demoiselle.xDemo.entity.Bookmark;

@Path("bookmark")
public class BookmarkREST {

	@Inject
	private BookmarkBC bc;

	@GET
	@Produces("application/json")
	public List<Bookmark> find(@QueryParam("q") String query) throws Exception {
		List<Bookmark> result;

		if (Strings.isEmpty(query)) {
			result = bc.findAll();
		} else {
			result = bc.find(query);
		}

		return result;
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Bookmark load(@PathParam("id") Long id) throws Exception {
		Bookmark result = bc.load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	@POST
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Produces("application/json")
	@Consumes("application/json")
	public Response insert(Bookmark body, @Context UriInfo uriInfo) throws Exception {
		checkId(body);

		String id = bc.insert(body).getId().toString();
		URI location = uriInfo.getRequestUriBuilder().path(id).build();

		return Response.created(location).entity(id).build();
	}

	@PUT
	@LoggedIn
	@Path("{id}")
	@Transactional
	@ValidatePayload
	@Produces("application/json")
	@Consumes("application/json")
	public void update(@PathParam("id") Long id, Bookmark body) throws Exception {
		checkId(body);
		load(id);

		body.setId(id);
		bc.update(body);
	}

	@DELETE
	@LoggedIn
	@Path("{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) throws Exception {
		load(id);
		bc.delete(id);
	}

	@DELETE
	@LoggedIn
	@Transactional
	public void delete(List<Long> ids) throws Exception {
		bc.delete(ids);
	}

	private void checkId(Bookmark entity) throws Exception {
		if (entity.getId() != null) {
			throw new BadRequestException();
		}
	}
	
	
	@GET
	@Path("/gerarRelatorioEXCEL")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@WebResult(name = "anexo")
	@Transactional
	public Response gerarRelatorioEXCEL(@Context HttpServletRequest request) throws Exception {

		ResponseBuilder response = Response.ok(bc.gerarRelatorioEXCEL());
		response.header("Content-disposition", "attachment; filename=relatorio_listagem_bookmark.xls");

		return response.build();

	}

	@GET
	@Path("/gerarRelatorioPDF")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@WebResult(name = "anexo")
	@Transactional
	public Response gerarRelatorioPDF(@Context HttpServletRequest request) throws Exception {


		String contexto = request.getRequestURL().toString();
		int posicao = contexto.indexOf("/rest/ServicosExternos");
		if (posicao != -1) {
			contexto = contexto.substring(0,posicao);
		}
		posicao = contexto.indexOf("/api/bookmark");
		if (posicao != -1) {
			contexto = contexto.substring(0,posicao);
		}

		
		ResponseBuilder response = Response.ok(	this.bc.gerarRelatorioPDFBookmark(contexto));
		response.header("Content-disposition", "attachment; filename=relatorio_listagem_bookmark.pdf");

		return response.build();
	}
	@GET
	@Path("/gerarGraficoPDFBarra")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@WebResult(name = "anexo")
	@Transactional
	public Response gerarGraficoPDFBarra(@Context HttpServletRequest request) throws Exception {


		String contexto = request.getRequestURL().toString();
		int posicao = contexto.indexOf("/rest/ServicosExternos");
		if (posicao != -1) {
			contexto = contexto.substring(0,posicao);
		}
		posicao = contexto.indexOf("/api/bookmark");
		if (posicao != -1) {
			contexto = contexto.substring(0,posicao);
		}

		
		String nomeJs = "demoBarra";
		String idGrafico = "barsChart";
		String legendaGrafico = "barsLegend";
			
		ResponseBuilder response = Response.ok(	this.bc.gerarRelatorioPDFGrafico(contexto, nomeJs, idGrafico, legendaGrafico));
		response.header("Content-disposition", "attachment; filename=graficoBarra.pdf");

		return response.build();
	}
	
	

}
