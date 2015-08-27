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
import org.apache.poi.hssf.util.HSSFColor;
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
import demoiselle.xDemo.util.PdfUtils;
import demoiselle.xDemo.util.Util;

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
	
//		FileInputStream file = new FileInputStream(new File(Util.dirCorrente() + "/modelo/modeloRecebimento.xls"));

		// Get the workbook instance for XLS file
		org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Bookmark");

//		// Create a new font and alter it.
//	    Font font = workbook.createFont();
//	    font.setFontHeightInPoints((short)14);
//	    font.setFontName("Courier New");
//	    font.setItalic(true);
////	    font.setStrikeout(true);
		
		HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
  //      hSSFFont.setFontHeightInPoints((short) 16);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        hSSFFont.setColor(HSSFColor.GREEN.index);
        
		CellStyle style = workbook.createCellStyle();
		style.setFont(hSSFFont);

		alterarValor(1, 1, sheet, "Listagem dos Dados do Bookmark",style);

		
		hSSFFont = workbook.createFont();
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
        style = workbook.createCellStyle();
		style.setFont(hSSFFont);

		alterarValor(3, 1, sheet, "Name", style);
		alterarValor(3, 2, sheet, "Salary", style);
		alterarValor(3, 3, sheet, "Description", style);
		alterarValor(3, 4, sheet, "Link", style);

		List<Bookmark> result = bc.findAll();
		if (result != null) {
			int linha = 4;
			for(Bookmark book : result) {
				alterarValor(linha, 1, sheet, book.getName(), null);
				alterarValor(linha, 2, sheet, book.getSalary().toString(), null);
				alterarValor(linha, 3, sheet, book.getDescription(), null);
				alterarValor(linha, 4, sheet, book.getLink(), null);
				linha++;
			}
		}		
		sheet.setColumnWidth(0, 5000); 
		sheet.setColumnWidth(1, 3000); 
		sheet.setColumnWidth(2, 3000); 
		sheet.setColumnWidth(3, 8000); 

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
		} finally {
			bos.close();
		}
		// byte[] bytes = bos.toByteArray();
		ResponseBuilder response = Response.ok(bos.toByteArray());
		response.header("Content-disposition", "attachment; filename=relatorio_listagem_bookmark.xls");

		return response.build();

	}
	
	private void alterarValor(int linha, int coluna, HSSFSheet sheet, String valor,CellStyle style) {
		Row rol = sheet.getRow(linha - 1);
		if (rol == null) {
			rol = sheet.createRow(linha - 1);
		}
		Cell cell = rol.getCell(coluna - 1);
		if (cell == null) {
			cell = rol.createCell(coluna - 1);
		}
		cell.setCellValue(new HSSFRichTextString(valor));
		if (style != null) {
			cell.setCellStyle(style);
		}
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

		StringBuilder sb = gerarHtml(contexto);

		String html = sb.toString();

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		PdfUtils.generatePDF(html, bOut, null);
		bOut.close();

		ResponseBuilder response = Response.ok(bOut.toByteArray());
		response.header("Content-disposition", "attachment; filename=relatorio_listagem_bookmark.pdf");

		return response.build();
	}

	public StringBuilder gerarHtml(String contexto) {

		String logoGsan = Util.dirCorrente() + "/images/logo_gsan.png";
		String logoCompesa = Util.dirCorrente() + "/images/logo_compesa.png";

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("	<head><title>Relatorio</title></head>");
		sb.append("	<body>");

		sb.append("		<table border='1' cellspacing='0' width='100%' height='40'>");
		sb.append("			<tr>");
		sb.append("				<td valign='top' width='100pt'>");
		sb.append("					<table border='0' width='100%' height='100%'>");
		sb.append("						<tr>");
		sb.append("							<td  valign='top'>");
		sb.append("								<img width='50pt' height='50pt' border='0' src='" + logoGsan + "'/>");
		sb.append("							</td>");
		sb.append("						</tr>");
		sb.append("					</table>");
		sb.append("				</td>");
		sb.append("				<td style='font-family:verdana; font-size:22px' valign='middle' align='center' width='*' >Listagem dos Dados do Bookmark </td>");
		sb.append("				<td  valign='top' align='right' width='100pt' >");
		sb.append("					<table border='0' width='100%' height='100%'>");
		sb.append("						<tr>");
		sb.append("							<td valign='top'>");
		sb.append("								<img width='80pt' height='50pt' border='0' src='" + logoCompesa + "'/>");
		sb.append("							</td>");
		sb.append("						</tr>");
		sb.append("					</table>");
		sb.append("				</td>");
		sb.append("			</tr>");
		sb.append("		</table>");

		sb.append("			<br/>");
		sb.append("			<br/>");
		
		sb.append("		<table border='0' cellspacing='0' width='100%' height='40'>");
		sb.append("			<tr bgcolor='#EFF8FB'>");
		sb.append("				<td bgcolor='#EFF8FB' style='font-family:verdana; font-size:16px'> Nome </td>");
		sb.append("				<td bgcolor='#EFF8FB' style='font-family:verdana; font-size:16px'> Salário </td>");
		sb.append("				<td bgcolor='#EFF8FB' style='font-family:verdana; font-size:16px'> Descricao </td>");
		sb.append("				<td bgcolor='#EFF8FB' style='font-family:verdana; font-size:16px'> Link </td>");
		sb.append("			</tr>");
		
		
		List<Bookmark> result = bc.findAll();
		if (result != null) {
			for(Bookmark book : result) {
				sb.append("			<tr>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='30%'>" + book.getName() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='20%'>" + book.getSalary().toString() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='20%'>" + book.getDescription() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='30%'>" + book.getLink() + "</td>");
				sb.append("			</tr>");
			}
		}

		sb.append("		</table>");

		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}

}
