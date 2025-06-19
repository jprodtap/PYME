package dav.AdministracionDelEfectivo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageMiddleConsultasAdef extends PageInicioMiddle {

	public PageMiddleConsultasAdef(PageLoginPymes parentPage) {
		super(parentPage);
	}
	
	PageAdministracionDelEfectivo pageAdministracionDelEfectivo = null;

	By locBotoneraAdef = By.xpath("//div[@class=\"navbar-collapse\"]");
	By locBtnAccesSys = By.xpath("//a[@href=\"/acessoSistema\"]");
	By locBtnEstadis = By.xpath("//a[@href=\"/estadisticas\"]");
	By locBtnAdef = By.xpath("//a[@href=\"/informeAdministracionEfectivo\"]");
	By locFiltAccSystem = By.xpath("//div[@class=\"col-sm-8 col-md-4\"]");
	By locFiltEstadi = By.xpath("//div[@class=\"col-sm-8 col-md-4\"]");
	
	By locMsgAlerta = By.xpath("//pre[@class=\"texto-msj\"]");
	
	String xpathRegAccSys = "(//div[@class=\"scroll-horizontal mb-2\"]/table/tr/td[5][contains(text(),'NOVEDAD')])[1]//following-sibling::td[1]";	
	
	//filtros Acceso al sistema
	By locNomUsuario = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[3]/input");
	By locTipoIdent = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[6]/select");
	By locClienteEmp = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[10]/input");
	By locTipoAutenti = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[18]/select");
	By locNumUsuario = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[8]/input");
	By locFechaDesde = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[12]/div/div/input[1]");
	By locFechaHasta = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[12]/div/div/input[2]");
	By locCanal = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[16]/select");
	
	By locBtnBuscarDis = By.xpath("//input[@value=\"Buscar Novedades\"]");
	By locOrganHora = By.xpath("//a[@href=\"#\"][contains(text(),'Fecha - Hora')]");
	By locCamposDeta = By.xpath("//td[@class=\"square__title\"]");
	
	//filtros Estadisticas
	By locTipoIDEmpresa = By.xpath("//div[@class=\"col-sm-8 col-md-4\"]/select");
	By locFechaInicio = By.xpath("(//input[@class=\"datepicker_input input__calendario\"])[1]");
	By locFechaFin = By.xpath("(//input[@class=\"datepicker_input input__calendario\"])[2]");
	By locNumCliEmpre = By.xpath("(//div[@class=\"col-sm-8 col-md-4\"]/input)[3]");
	By locNumIDEmpre = By.xpath("(//div[@class=\"col-sm-8 col-md-4\"]/input)[4]");
	
	By locBtnBuscarEst = By.xpath("//input[@value=\"Buscar\"]");
	By locCamposEsta = By.xpath("//div[@class=\"scroll-horizontal mb-2\"]/table/tr/th");
	
	//filtros Adef
	By locFechaInicioAd = By.xpath("(//input[@class=\"datepicker_input input__calendario\"])[1]");
	By locFechaFinAd = By.xpath("(//input[@class=\"datepicker_input input__calendario\"])[2]");
	By locNumApro = By.xpath("//div[@class=\"col-sm-8 col-md-4\"][4]/input");
	By locTextEstado = By.xpath("//b[contains(text(),'Estado')]");	
	String xpathNumAprobaAd = "//a[@href = \"#\"][contains(text(),'NUM_APROBA')]";
	
	
//	String fecha = "29/04/2024";
//	public static String horaInicio = "05:23:04 AM";
	
	String fecha;
	public static String horaInicio;
	public static String numAprob;

	public String InformeMiddleAdef() throws Exception {
		
		String nomUsuario = SettingsRun.getTestData().getParameter("Nombre Usuario").trim();
		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificacion").trim();
		String cliEmpresarial = SettingsRun.getTestData().getParameter("Cliente Empresa").trim();
		String tipoAutenticacion = SettingsRun.getTestData().getParameter("Tipo de Autenticacion").trim();
		String numIdUsuario = SettingsRun.getTestData().getParameter("Numero ID Usuario").trim();
		String canal = SettingsRun.getTestData().getParameter("Canal").trim();
		
		Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha = formatoFecha.format(fechaActual);
		
		String msgAlerta;
		int NumEstados;
		
		// Selecciona el modulo de Actividades Administradores
		this.irAOpcion("", "Informes", "Administración del efectivo");
		DXCUtil.wait(2);
		
		Evidence.save("informes middle_adef");
		
		this.getDriver().switchTo().frame("cphCuerpo_IframeADEF");
		
		do {
		} while (this.element(locBotoneraAdef) == null);
		
		if(this.element(locBotoneraAdef) != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Adef middle_se encuentra botonera inicial");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Adef middle_no se encuentra botonera inicial");
		}
		
		this.click(locBtnAccesSys);
		Evidence.save("botonera inicial");
		
		do {
		} while (this.element(locFiltAccSystem) == null);
		
		List<WebElement> Estado = this.findElements(locFiltAccSystem);
		NumEstados = Estado.size();

		if (NumEstados == 8) {
			Reporter.reportEvent(Reporter.MIC_PASS, "AccSystem middle_los filtros estan completos");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "AccSystem middle_faltan filtros");
		}
		
		Evidence.save("middle acessSystem_ filtros");
		
		do {
		} while (this.element(locNomUsuario) == null);
		
		this.write(locNomUsuario, nomUsuario);
		this.selectListItem(locTipoIdent, tipoIdentificacion);
		this.write(locClienteEmp, cliEmpresarial);
		this.selectListItem(locTipoAutenti, tipoAutenticacion);
		this.write(locNumUsuario, numIdUsuario);
		this.write(locFechaDesde, fecha);
		this.write(locFechaHasta, fecha); 
		this.selectListItem(locCanal, canal); 
		
		Evidence.save("middle acessSystem_ filtros diligenciados");
		
		this.click(locBtnBuscarDis);
		
		msgAlerta = this.msgAlerta();
		
		if (msgAlerta == null) {
		
			do {
			} while (this.element(locOrganHora) == null);
			
			this.click(locOrganHora);
			DXCUtil.wait(2);
			
			Evidence.save("middle acessSystem_ registros encontrados");
			
			this.ValidarFecha();
			msgAlerta = this.EstadisticasRegistro();
			
			if (msgAlerta != null) {
				Evidence.save("middle acessSystem_ error");
				return "error middle estadistica :"+msgAlerta;
			}
			
			msgAlerta = this.AdefRegistro();
			
			if (msgAlerta != null) {
				Evidence.save("middle acessSystem_ error");
				return "error middle Adef registro :"+msgAlerta;
			}
			
			return null;
		
		} else {
			Evidence.save("error middle acessSystem");
			return "error middle acessSystem :"+ msgAlerta;
		}
		
	}
	
	public void ValidarFecha() throws Exception {
		
		String HoraReg;
		String novedad = SettingsRun.getTestData().getParameter("Novedad").trim();
		int camposDetalle;
		
		xpathRegAccSys = xpathRegAccSys.replace("NOVEDAD", novedad);
		
		if (this.element(xpathRegAccSys) != null) {
			
			HoraReg = this.element(xpathRegAccSys).getText();
			HoraReg = HoraReg.substring(11);
			
			System.out.println(horaInicio);
			System.out.println(HoraReg);

	        // Formato de la hora
	        SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm:ss a");

	        // Convertir las horas a objetos Date
	        Date hInicio = formatoHora.parse(horaInicio);
	        Date hRegistro = formatoHora.parse(HoraReg);

	        // Comparar las horas
	        if (hRegistro.compareTo(hInicio) > 0) {
	   
	        	Reporter.reportEvent(Reporter.MIC_PASS, "AccSystem middle_Se enceuntra el tipo de novedad");
	        	xpathRegAccSys = xpathRegAccSys.substring(0, xpathRegAccSys.length()-3);
	        	xpathRegAccSys = xpathRegAccSys + "[5]/a";
	        	
	        	this.click(this.element(xpathRegAccSys));
	        	
	        	do {
	    		} while (this.element(locCamposDeta) == null);
	        	
	        	List<WebElement> numCampos = this.findElements(locCamposDeta);
	        	camposDetalle = numCampos.size();
	        	
	        	Evidence.save("middle AccSystem_detalles del registro");
	    		
	        	if (camposDetalle == 37) {
	    			Reporter.reportEvent(Reporter.MIC_PASS, "AccSystem middle_la informacion del detalle esta completa");
	    		} else {
	    			Reporter.reportEvent(Reporter.MIC_FAIL, "AccSystem middle_la informacion del detalle esta incompleta");
	    		}
	        	
	        } else if (hRegistro.compareTo(hInicio) <= 0) {
	        	Evidence.save("middle AccSystem_no se encunetra registro");
	        	Reporter.reportEvent(Reporter.MIC_FAIL, "AccSystem middle_no se enceuntra el registro");
	        }
			
		} else {
			Evidence.save("middle AccSystem_no se encunetra registro-novedad");
			Reporter.reportEvent(Reporter.MIC_FAIL, "AccSystem middle_no se enceuntra el tipo de novedad");
		}
		
	}
	
	public String EstadisticasRegistro() throws Exception {
		
		String tipoIDEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		String numIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		String numIdUsuario = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		
		String msgAlerta;
		int numEstados;
		int numTablaEstados;
		
		do {
		} while (this.element(locBtnEstadis) == null);
		
		this.click(locBtnEstadis);
		
		do {
		} while (this.element(locBtnBuscarEst) == null);
		
		Evidence.save("middle estadistica_pagoina de estadistica");
		
		this.selectListItem(locTipoIDEmpresa , tipoIDEmpresa);
		
		this.write(locFechaInicio , fecha);
		this.write(locFechaFin , fecha);
		
		this.write(locNumCliEmpre , numIdUsuario);
		this.write(locNumIDEmpre , numIDEmpresa);		
		
		this.click(locBtnBuscarEst);
		
		Evidence.save("middle estadistica_filtros diligenciados");
		
		msgAlerta = this.msgAlerta();
		
		if (msgAlerta == null) {
			
			List<WebElement> Estado = this.findElements(locFiltEstadi);
			numEstados = Estado.size();
			
			Evidence.save("middle estadistica_filtros de estado");
			if (numEstados == 5) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Estadistica middle_los filtros estan completos");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Estadistica middle_faltan filtros");
			}
			
			do {
			} while (this.element(locCamposEsta) == null);

			List<WebElement> tablaEstado = this.findElements(locCamposEsta);
			numTablaEstados = tablaEstado.size();
			
			Evidence.save("middle estadistica_informacion del registro");
			
			if (numTablaEstados == 10) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Estadistica middle_la informacion del registro esta completo");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Estadistica middle_la informacion del registro esta incompleto");
			}
			
			return null;
			
		} else {
			
			Evidence.save("error middle estadisticas");
			return "error middle estadistica :"+ msgAlerta;
			
		}

	}
	
	public String AdefRegistro() throws Exception {
		
		String msgAlerta;
		
		this.click(locBtnAdef);
		
		do {
		} while (this.element(locTextEstado) == null);

		this.write(locFechaInicioAd , fecha);
		this.write(locFechaFinAd , fecha);
		this.write(locNumApro , numAprob);
		this.click(locBtnBuscarEst);
		
		Evidence.save("middle adef registro_filtros diligenciados");
		
		msgAlerta = this.msgAlerta();
		
		if (msgAlerta == null) {
			Evidence.save("middle adef_registro encontrado");
			xpathNumAprobaAd = xpathNumAprobaAd.replace("NUM_APROBA", numAprob);
			if (this.element(xpathNumAprobaAd) != null) {
				this.click(this.element(xpathNumAprobaAd));
			}
			Evidence.save("middle adef registro encontrado");
			return null;
		} else {
			Evidence.save("middle adef registro_error");
			return msgAlerta;
		}
		
		
	}
	
	public String msgAlerta() throws Exception {
		
		int contador = 0;
		String msgalerta;
		do {
			contador++;
			DXCUtil.wait(1);
		} while (this.element(locMsgAlerta) == null && contador<6);
		
		if (contador == 6) {
			return null;
		} else {
			Evidence.save("Mensaje de alerta");
			msgalerta = this.element(locMsgAlerta).getText();
			return msgalerta;
		}
		
	}
}
