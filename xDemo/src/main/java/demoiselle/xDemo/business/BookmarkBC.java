package demoiselle.xDemo.business;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import demoiselle.xDemo.entity.Bookmark;
import demoiselle.xDemo.persistence.BookmarkDAO;
import demoiselle.xDemo.util.PdfUtils;
import demoiselle.xDemo.util.Util;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class BookmarkBC extends DelegateCrud<Bookmark, Long, BookmarkDAO> {

	private static final long serialVersionUID = 1L;

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Bookmark("Apple", 3233, "Vale do Silício",
					"http://www.apple.com"));
			insert(new Bookmark("IBM", 8903, "Hilston", "http://www.ibm.com"));
			insert(new Bookmark("AOL", 6340, "Chicago", "http://www.aol.com"));
			insert(new Bookmark("Foursquare", 350, "Detroit",
					"http://www.foursquare.com"));
			insert(new Bookmark("Twitter", 1403, "Boston",
					"http://www.twitter.com"));
			insert(new Bookmark("Google Inc", 7930, "California",
					"http://www.google.com"));
			insert(new Bookmark("Microsoft", 25434, "Michigan",
					"http://www.microsoft.com"));
			insert(new Bookmark("Amazon", 4565, "Nova York",
					"http://www.amazon.com"));
			insert(new Bookmark("Dell", 3454, "Arizona", "http://www.dell.com"));
			insert(new Bookmark("Facebook", 3434, "California",
					"http://www.facebook.com"));
			insert(new Bookmark("Alibaba", 24344, "Hong Kong",
					"http://www.alibaba.com"));
			insert(new Bookmark("Sales Force", 1233, "Paris",
					"http://www.salesforce.com"));
		}
	}

	public List<Bookmark> find(String filter) {
		return getDelegate().find(filter);
	}

	public byte[] gerarRelatorioEXCEL() throws Exception {

		org.apache.poi.hssf.usermodel.HSSFWorkbook workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Bookmark");

		HSSFFont hSSFFont = workbook.createFont();
		hSSFFont.setFontName(HSSFFont.FONT_ARIAL);

		hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		CellStyle style = workbook.createCellStyle();
		style.setFont(hSSFFont);

		alterarValor(1, 1, sheet, "Listagem dos Dados do Bookmark", style);

		hSSFFont = workbook.createFont();
		hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		style = workbook.createCellStyle();
		style.setFont(hSSFFont);

		alterarValor(3, 1, sheet, "Name", style);
		alterarValor(3, 2, sheet, "Salary", style);
		alterarValor(3, 3, sheet, "Description", style);
		alterarValor(3, 4, sheet, "Link", style);

		List<Bookmark> result = this.getDelegate().findAll();
		if (result != null) {
			int linha = 4;
			for (Bookmark book : result) {
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

		return bos.toByteArray();

	}

	private void alterarValor(int linha, int coluna, HSSFSheet sheet,
			String valor, CellStyle style) {
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

	public byte[] gerarRelatorioPDFBookmark(String contexto) throws Exception {

		StringBuilder sb = gerarHtmlPDF(contexto);

		String html = sb.toString();

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		PdfUtils.generatePDF(html, bOut, null);
		bOut.close();

		return bOut.toByteArray();
	}

	private StringBuilder gerarHtmlPDF(String contexto) {

		String logoGsan = contexto + "/images/logo_gsan.png";
		String logoCompesa = contexto + "/images/logo_compesa.png";

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
		sb.append("								<img width='50pt' height='50pt' border='0' src='"
				+ logoGsan + "'/>");
		sb.append("							</td>");
		sb.append("						</tr>");
		sb.append("					</table>");
		sb.append("				</td>");
		sb.append("				<td style='font-family:verdana; font-size:22px' valign='middle' align='center' width='*' >Listagem dos Dados do Bookmark </td>");
		sb.append("				<td  valign='top' align='right' width='100pt' >");
		sb.append("					<table border='0' width='100%' height='100%'>");
		sb.append("						<tr>");
		sb.append("							<td valign='top'>");
		sb.append("								<img width='80pt' height='50pt' border='0' src='"
				+ logoCompesa + "'/>");
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

		List<Bookmark> result = this.getDelegate().findAll();
		if (result != null) {
			for (Bookmark book : result) {
				sb.append("			<tr>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='30%'>"
						+ book.getName() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='20%'>"
						+ book.getSalary().toString() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='20%'>"
						+ book.getDescription() + "</td>");
				sb.append("				<td style='font-family:verdana; font-size:14px' width='30%'>"
						+ book.getLink() + "</td>");
				sb.append("			</tr>");
			}
		}

		sb.append("		</table>");

		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}

	public byte[] gerarRelatorioPDFGrafico(String contexto, String nomeJs, String idGrafico, String legendaGrafico) throws Exception {

//		StringBuilder sb = gerarHtmlGrafico("demoBarra","barsChart", "barsLegend");
		StringBuilder sb = gerarHtmlGrafico(contexto, nomeJs, idGrafico, legendaGrafico);

		String html = sb.toString();

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		PdfUtils.generatePDF(html, bOut, null);
		bOut.close();

		return bOut.toByteArray();
	}

	private StringBuilder gerarHtmlGrafico(String contexto, String nomeJs, String idGrafico, String legendaGrafico) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("	<head><title>Relatorio</title>");
		sb.append("	<link href=\"" +contexto +  "js/chart/css/demo.css\" rel=\"stylesheet\" type=\"text/css\"/>");
		sb.append("	<script src=\"" + contexto + "js/chart/vendor/Chart.js\"></script>");
		sb.append("	<script src=\"" + contexto + "js/chart/legend.js\"></script>");
		sb.append("	<script src=\"" + contexto + "js/chart/" + nomeJs + ".js\"></script>");
		sb.append("	</head>");
		sb.append("	<body>");

		sb.append("		<h2>Bars</h2>");
		sb.append("		<hr/>");
		sb.append("		<div>");
		sb.append("		    <canvas id=\"" + idGrafico + "\" width=\"600\" height=\"400\"></canvas>");
		sb.append("		    <div id=\"" + legendaGrafico + "\"></div>");
		sb.append("		</div>");
		sb.append("	");

		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}

}
