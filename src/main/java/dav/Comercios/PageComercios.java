package dav.Comercios;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiChannel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.AutogestionServicios.PageAutogestionServicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageComercios extends BasePageWeb {
	/**
	 * Pages y constructores
	 */
	PageOrigen pageOrigen = null;
	PageLoginPymes pageLogin = null;
	 
	static String mensajes = null;
	static int cont = 0;
	static String codigoUnico =null;
	static String franquicia =null;
	
	public PageComercios(BasePageWeb parentPage) throws Exception {
		super(parentPage);
		
		
		this.pageOrigen = new PageOrigen(this);
		this.pageLogin = new PageLoginPymes(this);
		// variables del archivo de lanzamiento que se utilizan en varios metodos
	    this.codigoUnico = SettingsRun.getTestData().getParameter("Código único").trim();
	    this.franquicia = SettingsRun.getTestData().getParameter("Franquicia").trim();
		 
		
	}
	
	//Locators generales que funcionan en varios modulos de Comercios
	
	By moduloComercios = By.xpath("//*[text()='Comercios']");
	By msjAlerta = By.id("lblMasterAlerta");
	By listCodigoUnico = By.id("cphCuerpo_ddlCodigoUnico");
	By listFranquicia = By.id("cphCuerpo_ddlFranquicia");
	By btnMostrarDatos = By.id("cphCuerpo_btnMostrarDatos");
	By inputRangoFechaDesde = By.id("cphCuerpo_txtFechaDesde");
	By inputRangoFechaHasta = By.id("cphCuerpo_txtFechaHasta");
	By tabladeResultados = By.id("cphCuerpo_gvResultado");
	By btnDescargarArchivo = By.id("cphCuerpo_btnGenerarDescarga");
	
	// Locators Módulo Consulta Detallada de ventas
	By checkBoxEstablecimiento = By.id("cphCuerpo_chbCadena");
	By listTipoFecha = By.id("cphCuerpo_ddlTipoFecha");
	By checkBoxVentas = By.id("cphCuerpo_Rb_Ventas");
	By checkBoxAjustes = By.id("cphCuerpo_Rb_Ajustes");
	By checkBoxRechazos = By.id("cphCuerpo_Rb_Rechazos");
	By listDetalleVentas = By.id("cphCuerpo_ddlDetalleVentas");
	By columnaTablaTipoTarjeta = By.xpath("//*[@id='cphCuerpo_gvResultado']/tbody/tr[1]/th[22]");
	By btnImprimirTabla = By.id("cphCuerpo_btnImprimir");
	By btnImprimir = By.id("idPrint");
	By btnSalir = By.xpath("//*[@id='form1']/table[1]/tbody/tr/td[3]/a");
	 
	  
	// Locators Módulo Datos Básicos
	By tablaDatosBasicos = By.xpath("//*[@id=\"cphCuerpo_pnlDatos\"]/table/tbody");
	By campoEstablemientoTabla = By.xpath("//*[@id=\"cphCuerpo_pnlDatos\"]/table/tbody/tr[3]");
	
	//Locators Módulo Consulta de Totales
	By checkBoxUltimas24Horas = By.id("cphCuerpo_rdbUltimas24Horas");
	By checkBoxUltimos7Dias = By.id("cphCuerpo_rdbUltimos7dias");
	By checkBoxUltimos15Dias = By.id("cphCuerpo_rdbUltimos15dias");
	By checkBoxUltimos30Dias = By.id("cphCuerpo_rdbUltimos30dias");
	By textMasCriteriosBusqueda = By.id("cphCuerpo_lnkBusquedaAvanzada");
	By listFormatoDescarga = By.id("cphCuerpo_dd_FormatosDescarga");
	By inputNombreArchivoAGenerar = By.id("cphCuerpo_Txt_NombreArchivo");
	
	// Locators Módulo Certificaciones de Establecimientos
	
	By checkBoxRetencionFuente = By.id("cphCuerpo_rdbRetencionFuente");
	By checkBoxReteIvaICA = By.id("cphCuerpo_rdbReteIVA");
	By listMes= By.id("cphCuerpo_ddlMes");
	By listAño = By.id("cphCuerpo_ddlAno");
	By btnDescargar = By.id("cphCuerpo_btnDescargar");
//***********************************************************************************************************************
public boolean validacionesComercios() throws Exception {
	
	String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo de Prueba").trim();
	Boolean resultado=false;
	Reporter.write("");
	Reporter.write("=============================[COMERCIOS]=============================");
	Reporter.write("");
	Reporter.write("");
	Reporter.write("Tipo de prueba"+"["+tipoPrueba+"]");
	DXCUtil.wait(4);
	
	// los metodos devuelven true en caso que la prueba sea exitosa
	switch (tipoPrueba) {
	case "Datos Básicos":
		resultado=datosBasicos();
		break;
	case "Consulta Detallada de Ventas":
		resultado=consultaDetalladaVentas();
		break;
	case "Consulta de Totales":
		resultado=consultaTotales();
		break;
	case "Certificaciones Comercios":
		resultado=certificacionesComercios();
		break;
		

	default:
		break;
	}
		
	
	return resultado;
}

//***********************************************************************************************************************

public boolean datosBasicos() throws Exception {
	boolean confirmarConsulta= false;
	Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: "+"Código único "+"["+codigoUnico+"]"+" Franquicia"+"["+franquicia+"]");	
	mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Datos Básicos");
	if (mensajes != null) {
		// Se realiza un intento adicional en caso que no sea posible ingresar el modulo
		if (this.element(moduloComercios)!=null) {
			this.mouseOver(moduloComercios);
			mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Consulta Detallada de Ventas");
			}
		if (mensajes != null) {
			return confirmarConsulta;
				
		}
	}
	cont=0;

	do {
		cont++;
		DXCUtil.wait(2);
	} while (this.element(listCodigoUnico)==null&&cont<5);
	cont=0;
	// se verifica si se presenta algún mensaje de error
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	this.selectListItem(listCodigoUnico, codigoUnico);
	this.selectListItem(listFranquicia, franquicia);
	DXCUtil.wait(5);
	Evidence.save("Resultados - Datos Básicos");
	if (this.element(msjAlerta)!=null) {
		String msjError= this.getText(msjAlerta);
		mensajes="Se visualiza mensaje: "+"["+msjError+"]";
		return confirmarConsulta;
	}
	if (this.element(campoEstablemientoTabla)==null) {
		mensajes="No se encontró resultados con los parámetros de busqueda suministrados.";
		Evidence.save("Resultados busqueda");
		return confirmarConsulta;
	} 
	  
	WebElement tablaResultados = this.findElement(tablaDatosBasicos);
	List<WebElement> filas = tablaResultados.findElements(By.tagName("tr")); 
	List<String> textosObtenidos = new ArrayList<>();
	String informacionTabla = "";
	for (int i=2; i<filas.size();i++) {
		WebElement fila = filas.get(i);
		informacionTabla = fila.getText();
		textosObtenidos.add(informacionTabla);
	}
	Reporter.reportEvent(Reporter.MIC_INFO, "Se encontró la siguiente información en la tabla");
	confirmarConsulta = true;
	for (String string : textosObtenidos) {
		Reporter.write(string);
	}
	mensajes = "Consulta de datos básicos exitosa.";
	
	return confirmarConsulta;
}

//=======================================================================================================================

public boolean consultaDetalladaVentas() throws Exception {
	
	 
	String establecimientoCadena = SettingsRun.getTestData().getParameter("Establecimiento de Cadena - Seleccionar checkbox").trim();
	String tipoFecha = SettingsRun.getTestData().getParameter("Tipo de fecha").trim();
	String selecciónCheckbox = SettingsRun.getTestData().getParameter("Selección Checkbox (Ventas-Ajustes-Rechazos)").trim();
	String rangoFechaDesde = SettingsRun.getTestData().getParameter("Rango Fecha Desde").trim();
	String rangoFechaHasta = SettingsRun.getTestData().getParameter("Rango Fecha Hasta").trim();
	String detalleVentas = SettingsRun.getTestData().getParameter("Detalle de ventas por").trim();
	String guardarArchivo = SettingsRun.getTestData().getParameter("Guardar Archivo").trim();
	
	Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: Código único"+"["+codigoUnico+"]"+" Establecimiento de Cadena "+"["+establecimientoCadena+"]"+" Franquicia "+"["+franquicia+"]"+" Tipo de fecha "+"["+tipoFecha+"]"+" Selección Checkbox "+"["+selecciónCheckbox+"]"+" Rango Fecha Desde "+"["+rangoFechaDesde+"]"+" Rango Fecha Hasta "+"["+rangoFechaHasta+"]"+" Detalle de Ventas "+"["+detalleVentas+"]");
	
	boolean confirmarConsulta= false;
	mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Consulta Detallada de Ventas");
	if (mensajes != null) {
		// Se realiza un intento adicional en caso que no sea posible ingresar el modulo
		if (this.element(moduloComercios)!=null) {
			this.mouseOver(moduloComercios);
			mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Consulta Detallada de Ventas");
			}
		if (mensajes != null) {
			return confirmarConsulta;
				
		}
	}
	cont=0;
	do {
		cont++;
		DXCUtil.wait(2);
	} while (this.element(listCodigoUnico)==null&&cont<5);
	cont=0;	
	// se verifica si se presenta algún mensaje de error
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	this.selectListItem(listCodigoUnico, codigoUnico);
	if (establecimientoCadena.contains("SI")) {
	this.click(checkBoxEstablecimiento);
	}else {
		
	}
	this.selectListItem(listFranquicia,franquicia);
	this.selectListItem(listTipoFecha, tipoFecha);
	if (selecciónCheckbox.contains("Ventas")) {
		this.click(checkBoxVentas);
	}else if (selecciónCheckbox.contains("Ajustes")) {
		this.click(checkBoxAjustes);
	}else if (selecciónCheckbox.contains("Rechazos")) {
		this.click(checkBoxRechazos);
	}
	DXCUtil.wait(2);
	this.write(inputRangoFechaHasta, rangoFechaHasta);
	DXCUtil.wait(1);
	this.write(inputRangoFechaDesde, rangoFechaDesde);
	this.selectListItem(listDetalleVentas, detalleVentas);
	Evidence.save("Parámetros de búsqueda");
	this.click(btnMostrarDatos);
	cont =0;
	do {
		cont++;
		DXCUtil.wait(1);
	} while (this.element(tabladeResultados)==null && cont<6);
	cont =0; 
	
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	Evidence.save("Resultados Busqueda");
	// se verifica que se presente tabla con resultados luego de realizar la busqueda
	if (this.element(tabladeResultados)==null) {
		mensajes = "No se encontró resultados con los parámetros de búsqueda suministrados.";
		return confirmarConsulta;
	}
	WebElement columTipoTarjeta = this.findElement(columnaTablaTipoTarjeta);
	this.focus(columTipoTarjeta);
	DXCUtil.wait(1);
	Evidence.save("Resultados busqueda 2");
	 
	if (guardarArchivo.equals("Imprimir")) {
		DXCUtil.wait(3);
		WebElement imprimirTabla = this.findElement(btnImprimirTabla);
		imprimirTabla.click();
		DXCUtil.wait(2);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se ingresa a la ventana para imprimir la tabla.");

		this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
		this.pageLogin.maximizeBrowser();

		DXCUtil.wait(2);
		Evidence.save("Evidencias imprimir archivo");
		this.findElement(btnImprimir).click();
		Evidence.saveFullPage("Evidencias imprimir archivo", this);
		
		DXCUtil.BonotesTecla("ENTER");
		 	
		// Nombre del archivo que va a aguardar
		String nombreArchivo = Evidence.getNbEvidenceDirectory() + "_Consulta_Detallada_De_Ventas";
		PageAutogestionServicioMiddle.toPrint(nombreArchivo);

		DXCUtil.wait(3);
		DXCUtil.BonotesTecla("TAB");
		DXCUtil.wait(1);
		DXCUtil.BonotesTecla("ENTER");
		cont = 0;
		do { 
			DXCUtil.wait(1);
			cont++;
		} while (this.element(btnSalir)==null&& cont<5);
		this.findElement(btnSalir).click();
		this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
		Reporter.reportEvent(Reporter.MIC_INFO, "Se imprimió correctamente.");
		



	} else if (guardarArchivo.equals("Descargar Archivo")) {
		DXCUtil.wait(1);
		this.findElement(btnDescargarArchivo).click();
		Reporter.reportEvent(Reporter.MIC_INFO, "Se descarga archivo con resultados de busqueda");

	}

	 
	
	WebElement tablaResultados = this.findElement(tabladeResultados);
	List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
	String [] infoFilas = null;
	int posiciones =1; 
	int index =0;
	String extraerInfo = "";
	int numeroCeldasTabla = 0;
	// se recorre todas las filas de la tabla  y se crea array con el número de filas que tiene la tabla
	for (WebElement fila:filas) {
		infoFilas = new String[posiciones];
		posiciones++;		
	} 
	// se extrae la información de las filas 
	for (WebElement fila:filas) {
		List<WebElement> celdas = tablaResultados.findElements(By.tagName("td"));
		numeroCeldasTabla = celdas.size();
		extraerInfo= fila.getText();
		infoFilas[index]=extraerInfo;
		index++;
	} 	
	
	if (infoFilas ==null) {
		mensajes = "No se encontró resultados con los parámetros de búsqueda suministrados";
	}else {
		// se genera reporte con  información encontrada en las filas de la tabla
		for (int i = 1; i	 < infoFilas.length; i++) {
			Reporter.reportEvent(Reporter.MIC_INFO,"Información encontrada en la(s) fila(s) de la tabla: " +"["+infoFilas[i]+"]");
		}
		// se verifica que la tabla siempre tenga las mismas columnas
		if (numeroCeldasTabla != 22) {
			Reporter.reportEvent(Reporter.MIC_INFO, "la tabla tiene más columnas de las esperadas, por favor verifique si hubo cambios en las columnas de la tabla");
		}
		mensajes = "Consulta detallada de ventas exitosa, se encontraron "+"["+(infoFilas.length-1)+"]" + " resultado(s) en la tabla";
		// se retorna true si se encontró información en la tabla
		confirmarConsulta = true;
	} 

	
 
	return confirmarConsulta; 
	
} 
//=======================================================================================================================
 

public boolean consultaTotales() throws Exception {
	
	String consultaMovimientosVentas = SettingsRun.getTestData().getParameter("Consulta de movimientos ventas");
	String rangoFechaDesde = SettingsRun.getTestData().getParameter("Rango Fecha Desde - Consulta de Totales");
	String rangoFechaHasta = SettingsRun.getTestData().getParameter("Rango Fecha Hasta - Consulta de Totales");
	String formatoDescarga = SettingsRun.getTestData().getParameter("Formato de Descarga");
	String nombreArchivoAGenerar = SettingsRun.getTestData().getParameter("Nombre de Archivo a Generar");
		
	boolean confirmarConsulta= false;
	int numeroCeldasTabla = 0;
	if (consultaMovimientosVentas.contains("Más criterios de búsqueda")) {
		Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: "+"Código único "+"["+codigoUnico+"]"+" Franquicia"+"["+franquicia+"]"+" Consulta movimientos Ventas: Rango Fecha Desde "+"["+rangoFechaDesde+"]"+" Rango Fecha Hasta "+"["+rangoFechaHasta+"]");
	}else {
		Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: "+"Código único "+"["+codigoUnico+"]"+" Franquicia"+"["+franquicia+"]"+" Consulta movimientos Ventas: "+"["+consultaMovimientosVentas+"]");
	}
	
	mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Consulta de Totales");
	if (mensajes != null) {
		// Se realiza un intento adicional en caso que no sea posible ingresar el modulo
		if (this.element(moduloComercios)!=null) {
			this.mouseOver(moduloComercios);
			mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Consulta Detallada de Ventas");
			}
		if (mensajes != null) {
			return confirmarConsulta;
				
		}
	}
	cont=0;
	do {
		cont++;
		DXCUtil.wait(2);
	} while (this.element(listCodigoUnico)==null&&cont<5);
	cont=0;
	// se verifica si se presenta algún mensaje de error
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	this.selectListItem(listCodigoUnico, codigoUnico);
	this.selectListItem(listFranquicia, franquicia);
	if (!consultaMovimientosVentas.contains("Más criterios de búsqueda")) {
		if (consultaMovimientosVentas.contains("Últimas 24 horas")) {
			this.click(checkBoxUltimas24Horas);
			
		}else if (consultaMovimientosVentas.contains("Últimos 7 días")) {
			this.click(checkBoxUltimos7Dias);
		}else if (consultaMovimientosVentas.contains("Últimos 15 días")) {
			this.click(checkBoxUltimos15Dias);
		}else if (consultaMovimientosVentas.contains("Últimos 30 días")) {
			this.click(checkBoxUltimos30Dias);
		}  
	}else {
		this.click(textMasCriteriosBusqueda);
		cont = 0;
		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(inputRangoFechaDesde) == null && cont<8);
		this.write(inputRangoFechaHasta, rangoFechaHasta);
		this.write(inputRangoFechaDesde, rangoFechaDesde);		
	}
	this.click(btnMostrarDatos);
	cont = 0;
	do {
		DXCUtil.wait(1);
		cont++;
	} while (this.element(tabladeResultados)==null && cont<6);
	
	Evidence.save("Resultados Busqueda");
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	// se verifica que se presente tabla con resultados luego de realizar la busqueda
	if (this.element(tabladeResultados)==null) {
		mensajes = "No se encontró resultados con los parámetros de búsqueda suministrados.";
		return confirmarConsulta;
	}
	
	
	
	WebElement tablaResultados = this.findElement(tabladeResultados);
	List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
	List<String> textosObtenidos = new ArrayList<>();
	String informacionTabla = "";
	List<WebElement> celdas = null;
	
	// se extrae la información de la tabla
	
	for (int i=1; i<filas.size();i++) {
		WebElement fila = filas.get(i);
		informacionTabla = fila.getText();
		textosObtenidos.add(informacionTabla);
		celdas = tablaResultados.findElements(By.tagName("td"));
		
	}
	// se genera reporte con información de las filas obtenidas en la tabla
	for (int i = 0; i < textosObtenidos.size(); i++) {
		Reporter.reportEvent(Reporter.MIC_INFO, "Información encontrada en la(s) fila(s) de la tabla: " +"["+textosObtenidos.get(i)+"]");
	}
	// se valida que la tabla tenga siempre las mismas columnas
	numeroCeldasTabla = celdas.size();
	if (numeroCeldasTabla != 4) {
		Reporter.reportEvent(Reporter.MIC_INFO, "la tabla tiene más columnas de las esperadas, por favor verifique si hubo cambios en las columnas de la tabla");
	}
	mensajes = "Consulta de totales exitosa, se encontraron "+"["+(textosObtenidos.size())+"]" + " resultado(s) en la tabla";
	this.selectListItem(listFormatoDescarga, formatoDescarga);
	this.write(inputNombreArchivoAGenerar,nombreArchivoAGenerar);
	this.click(btnDescargarArchivo);
	DXCUtil.wait(4);
	Reporter.reportEvent(Reporter.MIC_INFO, "Se descarga archivo con resultados de busqueda");
	
		// se retorna true si se encontró información en la tabla
		confirmarConsulta = true;
	
		
	return confirmarConsulta;

}

//=======================================================================================================================
public boolean certificacionesComercios() throws Exception {
	
	String checkBoxSeleccionado = SettingsRun.getTestData().getParameter("Selección checkbox Certificaciones Comercios");
	String año = SettingsRun.getTestData().getParameter("Año");
	String mes = SettingsRun.getTestData().getParameter("Mes");
	
	boolean confirmarConsulta= false;
	mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Certificaciones Comercios");
	if (mensajes != null) {
		// Se realiza un intento adicional en caso que no sea posible ingresar el modulo
		if (this.element(moduloComercios)!=null) {
			this.mouseOver(moduloComercios);
			mensajes =  this.pageOrigen.irAOpcion(null, "Comercios", "Certificaciones Comercios");
			}
		if (mensajes != null) {
			return confirmarConsulta;
		}
	}
	
	if (checkBoxSeleccionado.contains("Retención en la fuente")) {
		Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: "+"Código único: "+"["+codigoUnico+"]"+ " Checkbox a seleccionar "+ "["+checkBoxSeleccionado+"]"+" Año "+"["+año+"]");
		
	}else {
		Reporter.reportEvent(Reporter.MIC_INFO,"Parámetros de búsqueda: "+"Código único: "+"["+codigoUnico+"]"+ " Checkbox a seleccionar "+ "["+checkBoxSeleccionado+"]"+" Mes "+"["+mes+"]");
	}

	cont=0;
	do {
		cont++;
		DXCUtil.wait(2);
	} while (this.element(listCodigoUnico)==null&&cont<5);
	cont=0;
	//valida si se presenta falla al cargar el modulo
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	}
	this.selectListItem(listCodigoUnico, codigoUnico);
	if (checkBoxSeleccionado.contains("Retención en la fuente")) {
		this.click(checkBoxRetencionFuente);
		cont =0;
		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(listAño)==null&&this.element(listAño).isEnabled()&&cont<6);
		// Verificar si el elemento <select> tiene opciones
		
		if (this.element(listAño).findElements(By.tagName("option")).size() > 0) {
			
			this.selectListItem(listAño, año);
		}
		else { 
			mensajes = "No fue posible seleccionar el año, no es posible realizar búsqueda de certificado";
			Evidence.save("No permite seleccionar año");
			return confirmarConsulta;
		}
	}else {
		this.click(checkBoxReteIvaICA);
		cont =0;
		do {
			cont++;
			DXCUtil.wait(1);
		} while (this.element(listMes)==null&&this.element(listMes).isEnabled()&&cont<6);
		// Verificar si el elemento <select> tiene opciones
		if (this.element(listMes).findElements(By.tagName("option")).size() > 0) {
			this.selectListItem(listMes, mes);
		}else { 
			mensajes = "No fue posible seleccionar el mes, por favor verifique data";
			Evidence.save("No permite seleccionar mes");
			return confirmarConsulta;
		}
		 
	}
	
	this.click(btnMostrarDatos);
	DXCUtil.wait(4);
	Evidence.saveFullPage("Resultados búsqueda", this);
	// se valida si se presenta mensaje de alerta al realizar búsqueda
	if (this.element(msjAlerta)!=null) {
		mensajes = "Se visualiza mensaje: ["+this.getText(msjAlerta)+"]";
		Evidence.save("Mensaje de alerta");
		return confirmarConsulta;
	} 
	if (this.element(btnDescargar)!=null) {
		mensajes = "Consulta certificados de comercios exitosa, se descarga certificado";
		this.click(btnDescargar);
		DXCUtil.wait(5);
		// si se realiza todo el flujo de manera exitosa se cambia a true
		confirmarConsulta = true;
	}else {
		mensajes = "No fue posible realizar descarga de certificado";
	}
		
	return confirmarConsulta;  
} 
//=======================================================================================================================


//=======================================================================================================================
public static String getMensajes() {
	
	return mensajes;
}
//=======================================================================================================================


  
}
