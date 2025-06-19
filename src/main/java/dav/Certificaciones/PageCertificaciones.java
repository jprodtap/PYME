package dav.Certificaciones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.poifs.crypt.dsig.KeyInfoKeySelector;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.inject.Key;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import io.appium.java_client.Setting;

/**
 * Clase que contiene m�todos para la funcionalidad de la solicitud del
 * certificado.
 */
public class PageCertificaciones extends BasePageWeb {

	/**
	 * Variables de otras clases para llamar algunos métodos que tienen alguna
	 * relación.
	 */
	PageOrigen pageOrigen = null;
	PageConfirmacion pageConfirmacion = null;
	PagePortalPymes pagePortalPymes = null;  

	public PageCertificaciones(BasePageWeb parentPage) {
		super(parentPage);
	}

	/**
	 * Locators que se encontratar en Certificaciones y Paz y Salvos
	 */

	// Locator para seleccionar un tipo de producto
	By idTipoProducto = By.id("cphCuerpo_ddTipoProducto");

	// Locator de producto
	By selectProducto = By.id("cphCuerpo_ddProducto");

	/**
	 * 
	 * Caracteristicas del certificado, son todas usables, pero en algunos casos no
	 * apareceran
	 * 
	 **/
	// Locators con o sin sueldo
	final By mostrarSaldo = By.xpath("//*[@id='cphCuerpo_rblCaracteristicasSaldo']/input[@value='OK_SALDO']");
	final By noMostrarSaldo = By.xpath("//*[@id='cphCuerpo_rblCaracteristicasSaldo']/input[@value='NO_SALDO']");
	// Locators para seleccionar el idioma
	final By idiomaEspanyol = By.xpath("//*[@id='cphCuerpo_rblCaracteristicasIdioma']/input[@value='ES']");
	final By idiomaIngles = By.xpath("//*[@id='cphCuerpo_rblCaracteristicasIdioma']/input[@value='EN']");

	// Locators para quien va dirigido el certificado funcionan para Certificación
	// comercial de productos vigentes
	final By aQuienIntereseComercial = By
			.xpath("//*[@id='cphCuerpo_rblCaracteristicasDirigidoCCPV']/input[@value='NN']");
	final By dirigidoAComercial = By
			.xpath("//*[@id='cphCuerpo_rblCaracteristicasDirigidoCCPV']/span/input[@value='DIR']");
	By envioParaComercial = By.id("cphCuerpo_txtDirigidoCCPV");

	// Locators para quien va dirigido el certificado funcionan para Certificación
	// saldo para productos de crédito
	final By aQuienInterese = By
			.xpath("//*[@id='cphCuerpo_rblCaracteristicasDirigidoPC']/input[@value='NNcphCuerpo_txtDirigidoCCPV']");
	final By dirigidoA = By.xpath("//*[@id='cphCuerpo_rblCaracteristicasDirigidoPC']/span/input[@value='DIR']");
	By envioPara = By.id("cphCuerpo_txtDirigidoPC");

	By ciudadEmision = By.id("cphCuerpo_ddlciudad");
	// Locator donde se ubican las opciones para cada tipo de producto o proceso
	By btnDescargar = By.id("cphCuerpo_btnMostrarDatos");

	// Locators certificaciones tributarias
	final By hasta2017 = By.xpath("//*[@id='cphCuerpo_rblCertificaciontributaria']//input[@value='2017']");
	final By desde2018 = By.xpath("//*[@id='cphCuerpo_rblCertificaciontributaria']//input[@value='2018']");

	// Este varia dependiendo el caso, los dos son usables depende
	By anyoTributario = By.id("cphCuerpo_ddlAñoTributarias");
	By anyoTributario2 = By.id("cphCuerpo_ddlAñoTributarias2");

	// Locators Certificaciones anuales unificadas
	By certificadoUnificado = By.id("cphCuerpo_certUnificada");
	By anyoCertUnificado = By.id("cphCuerpo_ddlAñoRepUnificado");
	By seleccionAnual = By.id("cphCuerpo_ddlAñoRepAnual");

	// Locators Certificaciones GMF
	By fondoInversionFIC = By.id("cphCuerpo_ddlProductosFIC");
	By bancoFIC = By.id("cphCuerpo_ddlBancoFIC");
	By tipoProductoFIC = By.id("cphCuerpo_ddlTipoProductoFIC");
	By numeroProductoFIC = By.id("cphCuerpo_txtNumeroProductoFIC");
	By aceptResponsabilidad = By.id("cphCuerpo_chkAceptacionResponsabilidad");
	By informacionFIC = By.xpath("//*[@id='cphCuerpo_pnlAceptacionResponsabilidad']/label/span");

	/**
	 * Locators Planes Institucionales
	 */
//		Locators generales para realizar cualquiera de los tipos de consulta

	By btnConsultar = By.id("cphCuerpo_btnConsultar");
	By tipoConsulta = By.id("cphCuerpo_ddlTipoConsulta");
	By Descargar = By.xpath("//*[@id=\"lblMasterAlerta\"]/a");
	By mensajeAlerta = By.id("lblMasterAlerta");
	By estadoDescarga = By.id("cphCuerpo_gvDescargas_lnkEstado_0");
	By estadoDescarga2 = By.xpath("//*[@id=\"cphCuerpo_gvDescargas\"]/tbody/tr");
	By linkDescarga = By.linkText("Procesado");

	// Locators Tipo de consulta: Saldos Generales

	By referenciaSaldoGen = By.id("cphCuerpo_dropReferenciaSG");
	By fechaDeConsultaSaldoGen = By.id("cphCuerpo_txtFechaConsultaSG");
	By fechaDeAperturaSaldoGen = By.id("cphCuerpo_txtFechaAperturaSG");
	By verDetalle = By.id("cphCuerpo_gvSaldosGenerales_lnkVerDetalle_0");

	// Locators Tipo de consulta: Consulta por participe

	By referenciaConsultaPartici = By.id("cphCuerpo_dropReferenciaCP");
	By tipoIdentificacionPartici = By.id("cphCuerpo_dropTipoIdentificacionCP");
	By numIdentificacionPartici = By.id("cphCuerpo_txtNumeroIdentificacionCP");
	By numCuentaIndividualPartici = By.id("cphCuerpo_dropCuentaCP");
	By fechaAperturaConsulPartici = By.id("cphCuerpo_txtFechaAperturaCP");
	By tipoConsultaPartici = By.id("cphCuerpo_dropTipoConsultaCP");
	By fechaConsultaPartici = By.id("cphCuerpo_txtFechaConsultaCP");
	By btnGuardar = By.id("cphCuerpo_btnGuardar");
	By periodoCertTrib = By.id("cphCuerpo_dropPeriodoCP");
	By mesExtracto = By.id("cphCuerpo_dropMesExtractoCP");

	// Locators Tipo de consulta: Consulta Retiros y Consulta de Aportes

	By consultadoPor = By.id("cphCuerpo_dropConsultaCR");
	By referencia = By.id("cphCuerpo_dropReferenciaCR");
	By fechaInicial = By.id("cphCuerpo_txtFechaInicialCR");
	By fechaFinal = By.id("cphCuerpo_txtFechaFinalCR");
	By tipoIdentificacion = By.id("cphCuerpo_dropTipoIdentificacionCR");
	By numIdentificacion = By.id("cphCuerpo_txtNumeroIdentificacionCR");
	By numCuentaIndividual = By.id("cphCuerpo_dropCuentaCR");

// Locators Tipo de consulta: Total Participes

	By slcReferenciaTP = By.xpath("//*[@id='cphCuerpo_dropReferenciaTP']");
	By fechaConsultaTP = By.id("cphCuerpo_txtFechaConsultaTP");

	int tiempoEspera = 10;
	int tiempoTranscurrido = 0;

	/**
	 * Método que permite identificar la operación que se realizara para el
	 * certificado, dependiendo la información de excel
	 */

	public void solicitarTipoCertificado() throws Exception {
		String certificado = SettingsRun.getTestData().getParameter("Clase de Certificados");

		switch (certificado) {
		case "Certificaciones y Paz y Salvos":
			this.certificadosPazySalvos();
			break;

		case "Planes Institucionales":
			this.planesInstitucionales();
			break;

		default:
			break;
		}

	}

	/**
	 * Metodo que permite ingresar a Consultas/ Certificados y Paz y Salvos
	 * 
	 */
	private void certificadosPazySalvos() throws Exception {
		// Ingreso a ruta
		this.pageOrigen = new PageOrigen(this);
		this.pageOrigen.irAOpcion("", "Consultas", "Certificaciones y Paz y Salvos");

		// Variables que seran identificados para realizar el certificado
		String tipoProducto = SettingsRun.getTestData().getParameter("Tipo de producto").trim();
		String tipoCertificado = SettingsRun.getTestData().getParameter("Tipo de Certificado").trim();

		// Valida si no hay error al cargar la página

		this.mensajeError();
		// Valida si el selector contiene información para seleccionar
		this.validarOpcionesLocator(idTipoProducto);
		WebElement selectTipoProducto = null;

		while (tiempoTranscurrido < tiempoEspera) {
			try {
				selectTipoProducto = this.findElement(idTipoProducto);

				if (selectTipoProducto != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
				}
			}
		}

		if (selectTipoProducto != null) {
			this.selectListItem(selectTipoProducto, tipoProducto);
		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se encontró el elemento.");
		}

		Evidence.save("Evidencia certificados");
		Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó el tipo de producto: [ " + tipoProducto + " ].");
		this.mensajeError();

		// Metodo que selecciona el tipo de certificado para descargar
		this.tipoCertificados();

		// Metodo que realiza la función para descargar algun tipo de certificado
		this.certificadoCaracteristicas(tipoCertificado);
		this.mensajeError();

		// Valida si se encuentra el botón de descargar
		WebElement descargarBtn = this.findElement(btnDescargar);

		if (descargarBtn != null) {
			descargarBtn.click();
			this.mensajeError();
			Reporter.reportEvent(Reporter.MIC_PASS, "Se ha descargado el certificado correctamente.");
		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se encontró el elemento.");

		}

		Evidence.saveAllScreens("Evidencias", this);
		this.pagePortalPymes = new PagePortalPymes(this);
		this.pagePortalPymes.terminarIteracion();
	}

	// Método que permite ingresar a Consultas/ Planes Institucionales
	private void planesInstitucionales() throws Exception {
		// Ingreso a ruta

		this.pageOrigen = new PageOrigen(this);
		Evidence.save("Ingreso exitoso");
		this.pageOrigen.irAOpcion("", "Consultas", "Planes Institucionales");

		// para cualquier flujo de consulta de planes institucionales

		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo de consulta");
		String numReferencia = SettingsRun.getTestData().getParameter("Referencia");
		String fechaConsulta = SettingsRun.getTestData().getParameter("Fecha de consulta");

		// consulta por Participe
		String tipoIdentificacionPart = SettingsRun.getTestData().getParameter("Tipo de identificación Participe");
		String numIdentificacionPart = SettingsRun.getTestData().getParameter("Número de identificación Participe");
		String numCuentaIndivPart = SettingsRun.getTestData().getParameter("Número Cuenta Individual Participe");
		String tipoConsultaPart = SettingsRun.getTestData().getParameter("Tipo de consulta por Participe");
		String mesExtractos = SettingsRun.getTestData().getParameter("Mes Extracto");
		String periodoCertificaciones = SettingsRun.getTestData().getParameter("Periodo - Certificaciones");

		// consulta por retiros o aportes
		String consultaPorRetiAport = SettingsRun.getTestData()
				.getParameter("Consulta Por - Consulta Retiros o Aportes");
		String fechaInicialRetiAport = SettingsRun.getTestData()
				.getParameter("Fecha inicial - Consulta Retiros o Aportes");
		String fechaFinalRetiAport = SettingsRun.getTestData()
				.getParameter("Fecha Final - Consulta Retiros o Aportes");

		switch (tipoPrueba) {
		case "Saldos Generales":
			this.saldosGenerales(tipoPrueba, fechaConsulta, numReferencia);
			break;
		case "Consulta por Participe":
			this.consultaPorParticipe(tipoPrueba, numReferencia, tipoIdentificacionPart, numIdentificacionPart,
					numCuentaIndivPart, tipoConsultaPart, fechaConsulta, mesExtractos, periodoCertificaciones);
			break;
		case "Consulta Retiros":
			this.consultaRetiros(tipoPrueba, numReferencia, consultaPorRetiAport, fechaInicialRetiAport,
					fechaFinalRetiAport, tipoIdentificacionPart, numIdentificacionPart, numCuentaIndivPart);
			break;
		case "Consulta Aportes":
			this.consultaAportes(tipoPrueba, numReferencia, consultaPorRetiAport, fechaInicialRetiAport,
					fechaFinalRetiAport, tipoIdentificacionPart, numIdentificacionPart, numCuentaIndivPart);
			break;
		case "Total Participes":
			this.totalParticipes(tipoPrueba, numReferencia, fechaConsulta);
			break;

		default:
			break;
		}

	}

	private void saldosGenerales(String tipoPrueba, String fechaConsulta, String numReferencia) throws Exception {
		Reporter.reportEvent(Reporter.MIC_INFO, "Datos a consultar: Tipo de prueba - [" + tipoPrueba
				+ "] - Número de referencia - [" + numReferencia + "] - Fecha de consulta - [" + fechaConsulta + "]");
		this.selectListItem(tipoConsulta, tipoPrueba);

		validarReferencia(referenciaSaldoGen, numReferencia);
		do {
			DXCUtil.wait(1);
		} while (this.element(fechaDeConsultaSaldoGen) == null);

		String fechaApertura = this.element(fechaDeAperturaSaldoGen).getAttribute("value");
		Reporter.reportEvent(Reporter.MIC_INFO, "Fecha de Apertura: " + fechaApertura);
		this.write(fechaDeConsultaSaldoGen, fechaConsulta);
		Evidence.save("Consulta Planes institucionales: Saldos Generales");
		this.findElement(btnConsultar).click();

		DXCUtil.wait(4);

		if (this.element(mensajeAlerta) != null) {
			String msjAlerta = this.getText(mensajeAlerta);
			if (mensajeAlerta != null
					&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
				this.mensajeError();
			}
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(verDetalle) == null);
		this.click(verDetalle);
		DXCUtil.wait(3);
		Evidence.save("Mensaje de descarga");
		String msjAlerta2 = this.getText(mensajeAlerta);
		Reporter.reportEvent(Reporter.MIC_INFO, msjAlerta2);
		this.click(Descargar);
		DXCUtil.wait(5);
		// Valida si el certificado de planes institucionales cambia de estado a
		// procesado
		DescargaPlanesInstitucionales();

		Evidence.save("Descargas");
		this.closeCurrentBrowser();
		SettingsRun.exitTestIteration();

	}

	private void consultaPorParticipe(String tipoPrueba, String numReferencia, String tipoIdentificacionPart,
			String numIdentificacionPart, String numCuentaIndivPart, String tipoConsultaPart, String fechaConsulta,
			String mesExtractos, String periodoCertificaciones) throws Exception {

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Datos a consultar: Tipo de prueba - [" + tipoPrueba + "] - Número de referencia - [" + numReferencia
						+ "] - Fecha de consulta - [" + fechaConsulta + "] - Tipo de consulta - [" + tipoConsultaPart
						+ "]");
		this.selectListItem(tipoConsulta, tipoPrueba);
		do {
			DXCUtil.wait(1);
		} while (this.element(referenciaConsultaPartici) == null);

		validarReferencia(referenciaConsultaPartici, numReferencia);

		do {
			DXCUtil.wait(1);
		} while (this.element(tipoIdentificacionPartici) == null);
		this.selectListItem(tipoIdentificacionPartici, tipoIdentificacionPart);
		DXCUtil.wait(2);
		this.write(numIdentificacionPartici, numIdentificacionPart);
		DXCUtil.wait(2);
		this.mouseOver(this.element(tipoIdentificacionPartici));
		this.mouseClick();
		this.write(numIdentificacionPartici, numIdentificacionPart);
		do {
			DXCUtil.wait(1);
		} while (this.element(numCuentaIndividualPartici) == null);

		String msg = this.seleOptionGetAttribu(numCuentaIndividualPartici, numCuentaIndivPart);
		if (msg == null || msg.equals("")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el número de cuenta individual "
					+ numCuentaIndivPart + " verifique archivo de lanzamiento ");
			Evidence.save("Número de cuenta individual no encontrado");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();
		}

		do {

			DXCUtil.wait(1);
		} while (this.element(tipoConsultaPartici) == null);

		this.selectListItem(tipoConsultaPartici, tipoConsultaPart);
		DXCUtil.wait(10);
		if (tipoConsultaPart.equals("Saldos")) {

			this.write(fechaConsultaPartici, fechaConsulta);
			Evidence.save("Consulta por Participe");
			this.click(btnConsultar);
			DXCUtil.wait(4);

			if (this.element(mensajeAlerta) != null) {
				String msjAlerta = this.getText(mensajeAlerta);
				if (mensajeAlerta != null
						&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
					this.mensajeError();
				}
			}
			do {
				DXCUtil.wait(1);
			} while (this.element(btnGuardar) == null);
			this.click(btnGuardar);
			DXCUtil.wait(15);
			Evidence.save("Consulta por Participe: Saldos");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realiza consulta por participe Saldos, se descarga archivo");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		} else if (tipoConsultaPart.equals("Extractos")) {
			DXCUtil.wait(4);
			String msgExtracto = this.seleOptionGetAttribu(mesExtracto, mesExtractos);
			if (msgExtracto == null || msgExtracto.equals("")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se seleccionó el mes extracto " + mesExtractos); 
				Evidence.save("Mes extracto no encontrado");
				this.closeCurrentBrowser();
				SettingsRun.exitTestIteration();
			}
			DXCUtil.wait(2);
			Evidence.save("Consulta por participe: Extractos");
			this.click(btnConsultar);
			DXCUtil.wait(4);

			if (this.element(mensajeAlerta) != null) {
				String msjAlerta = this.getText(mensajeAlerta);
				if (mensajeAlerta != null
						&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
					this.mensajeError();
				}
			}
			DXCUtil.wait(15);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realiza consulta por participe Extractos, se descarga archivo");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		} else if (tipoConsultaPart.equals("Certificaciones Tributarias")) {
			DXCUtil.wait(2);
			String msgPeriodoCert = this.seleOptionGetAttribu(periodoCertTrib, periodoCertificaciones);
			if (msgPeriodoCert == null || msgPeriodoCert.equals("")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se seleccionó campo Periodo " + periodoCertificaciones); 
				Evidence.save("Campo Periodo no encontrado");
				this.closeCurrentBrowser();
				SettingsRun.exitTestIteration();
			}

			Evidence.save("Certificaciones Tributarias");
			this.click(btnConsultar);
			DXCUtil.wait(4);

			if (this.element(mensajeAlerta) != null) {
				String msjAlerta = this.getText(mensajeAlerta);
				if (mensajeAlerta != null
						&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
					this.mensajeError();
				}
			}
			DXCUtil.wait(15);
			Reporter.reportEvent(Reporter.MIC_PASS,
					"Se realiza consulta por participe Certificaciones Tributarias, se descarga archivo");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();
		}
	}

	private void consultaRetiros(String tipoPrueba, String numReferencia, String consultaPorRetiAport,
			String fechaInicialRetiAport, String fechaFinalRetiAport, String tipoIdentificacionPart,
			String numIdentificacionPart, String numCuentaIndivPart) throws Exception {

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Datos a consultar: Tipo de prueba - [" + tipoPrueba + "] - Consulta Por - [" + consultaPorRetiAport
						+ "] - Número de referencia - [" + numReferencia + "] - Fecha inicial - ["
						+ fechaInicialRetiAport + "] - Fecha final - [" + fechaFinalRetiAport + "]");

		this.selectListItem(tipoConsulta, tipoPrueba);
		do {
			DXCUtil.wait(1);
		} while (this.element(consultadoPor) == null);

		this.selectListItem(consultadoPor, consultaPorRetiAport);

		if (consultaPorRetiAport.equals("Totales")) {
			do {
				DXCUtil.wait(1);
			} while (this.element(referencia) == null);

			validarReferencia(referencia, numReferencia);

			do {
				DXCUtil.wait(1);
			} while (this.element(fechaInicial) == null);
			this.write(fechaInicial, fechaInicialRetiAport);
			this.write(fechaFinal, fechaFinalRetiAport);
			Evidence.save("Consulta Planes institucionales: Retiros");
			this.click(btnConsultar);
			do {
				DXCUtil.wait(1);
			} while (this.element(mensajeAlerta) == null);

			String msjAlerta = this.getText(mensajeAlerta);
			if (mensajeAlerta != null
					&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
				this.mensajeError();
			}  
			Evidence.save("Mensaje de descarga");
			Reporter.reportEvent(Reporter.MIC_INFO, msjAlerta);
			this.click(Descargar);
			DXCUtil.wait(5);
			DescargaPlanesInstitucionales();
			Evidence.save("Descargas");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		} else if (consultaPorRetiAport.equals("Por Partícipe")) {
			do {
				DXCUtil.wait(1);
			} while (this.element(referencia) == null);
			// Valida si el número de referencia suministrado en el archivo de lanzamiento
			// se encuentra en la lista del campo referencia
			validarReferencia(referencia, numReferencia);

			do {
				DXCUtil.wait(1);
			} while (this.element(tipoIdentificacion) == null);
			this.selectListItem(tipoIdentificacion, tipoIdentificacionPart);
			DXCUtil.wait(2);
			this.write(numIdentificacion, numIdentificacionPart);
			DXCUtil.wait(2);
			this.mouseOver(this.element(tipoIdentificacion));
			this.mouseClick();
			this.write(numIdentificacion, numIdentificacionPart);
			do {
				DXCUtil.wait(1);
			} while (this.element(numCuentaIndividual) == null);
			String msg = this.seleOptionGetAttribu(numCuentaIndividual, numCuentaIndivPart);
			if (msg == null || msg.equals("")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el número de cuenta individual "
						+ numCuentaIndivPart + " verifique archivo de lanzamiento ");
				Evidence.save("Número de cuenta individual no encontrado");
				this.closeCurrentBrowser();
				SettingsRun.exitTestIteration();
			}
			do {
				DXCUtil.wait(1);
			} while (this.element(fechaInicial) == null);
			this.write(fechaInicial, fechaInicialRetiAport);
			this.write(fechaFinal, fechaFinalRetiAport);
			Evidence.save("Consulta Retiros: Por Partícipe");
			this.click(btnConsultar);
			DXCUtil.wait(4);

			if (this.element(mensajeAlerta) != null) {
				String msjAlerta = this.getText(mensajeAlerta);
				if (mensajeAlerta != null
						&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
					this.mensajeError();
				}
			}
			do {
				DXCUtil.wait(1);
			} while (this.element(btnGuardar) == null);
			this.click(btnGuardar);
			DXCUtil.wait(15);
			Evidence.save("Consulta Retiros: Por Partícipe");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realiza consulta retiros Por Partícipe, se descarga archivo");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		}
	}

	private void consultaAportes(String tipoPrueba, String numReferencia, String consultaPorRetiAport,
			String fechaInicialRetiAport, String fechaFinalRetiAport, String tipoIdentificacionPart,
			String numIdentificacionPart, String numCuentaIndivPart) throws Exception {

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Datos a consultar: Tipo de prueba - [" + tipoPrueba + "] - Consulta Por - [" + consultaPorRetiAport
						+ "] - Número de referencia - [" + numReferencia + "] - Fecha inicial - ["
						+ fechaInicialRetiAport + "] - Fecha final - [" + fechaFinalRetiAport + "]");

		this.selectListItem(tipoConsulta, tipoPrueba);
		do {
			DXCUtil.wait(1);
		} while (this.element(consultadoPor) == null);

		this.selectListItem(consultadoPor, consultaPorRetiAport);

		if (consultaPorRetiAport.equals("Totales")) {
			do {
				DXCUtil.wait(1);
			} while (this.element(referencia) == null);

			validarReferencia(referencia, numReferencia);

			do {
				DXCUtil.wait(1);
			} while (this.element(fechaInicial) == null);
			this.write(fechaInicial, fechaInicialRetiAport);
			this.write(fechaFinal, fechaFinalRetiAport);
			Evidence.save("Consulta Planes institucionales: Retiros");
			this.click(btnConsultar);
			do {
				DXCUtil.wait(1);
			} while (this.element(mensajeAlerta) == null);

			String msjAlerta = this.getText(mensajeAlerta);
			if (mensajeAlerta != null
					&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
				this.mensajeError();
			}
			Evidence.save("Mensaje de descarga");
			Reporter.reportEvent(Reporter.MIC_INFO, msjAlerta);
			this.click(Descargar);
			DXCUtil.wait(5);
			DescargaPlanesInstitucionales();
			Evidence.save("Descargas");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		} else if (consultaPorRetiAport.equals("Por Partícipe")) {
			do {
				DXCUtil.wait(1);
			} while (this.element(referencia) == null);
			// Valida si el número de referencia suministrado en el archivo de lanzamiento
			// se encuentra en la lista del campo referencia
			validarReferencia(referencia, numReferencia);

			do {
				DXCUtil.wait(1);
			} while (this.element(tipoIdentificacion) == null);
			this.selectListItem(tipoIdentificacion, tipoIdentificacionPart);
			DXCUtil.wait(2);
			this.write(numIdentificacion, numIdentificacionPart);
			DXCUtil.wait(2);
			this.mouseOver(this.element(tipoIdentificacion));
			this.mouseClick();
			this.write(numIdentificacion, numIdentificacionPart);
			do {
				DXCUtil.wait(1);
			} while (this.element(numCuentaIndividual) == null);

			String msg = this.seleOptionGetAttribu(numCuentaIndividual, numCuentaIndivPart);
			if (msg == null || msg.equals("")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el número de cuenta individual "
						+ numCuentaIndivPart + " verifique archivo de lanzamiento ");
				Evidence.save("Número de cuenta individual no encontrado");
				this.closeCurrentBrowser();
				SettingsRun.exitTestIteration();
			}

			do {
				DXCUtil.wait(1);
			} while (this.element(fechaInicial) == null);
			this.write(fechaInicial, fechaInicialRetiAport);
			this.write(fechaFinal, fechaFinalRetiAport);
			Evidence.save("Consulta Aportes: Por Partícipe");
			this.click(btnConsultar);
			DXCUtil.wait(4);

			if (this.element(mensajeAlerta) != null) {
				String msjAlerta = this.getText(mensajeAlerta);
				if (mensajeAlerta != null
						&& !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
					this.mensajeError();
				}
			}
			do {
				DXCUtil.wait(1);
			} while (this.element(btnGuardar) == null);
			this.click(btnGuardar);
			DXCUtil.wait(15);
			Evidence.save("Consulta Aportes: Por Partícipe");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realiza consulta aportes Por Partícipe, se descarga archivo");
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		}

	}

	private void totalParticipes(String tipoPrueba, String numReferencia, String fechaConsulta) throws Exception {
		this.pagePortalPymes = new PagePortalPymes(this);

		Reporter.reportEvent(Reporter.MIC_INFO, "Datos a consultar: Tipo de prueba - [" + tipoPrueba
				+ "] - Número de referencia - [" + numReferencia + "] - Fecha de consulta - [" + fechaConsulta + "]");
		this.selectListItem(tipoConsulta, tipoPrueba);
		DXCUtil.wait(4);
		// Valida si el número de referencia suministrado en el archivo de lanzamiento
		// se encuentra en la lista del campo referencia
		validarReferencia(slcReferenciaTP, numReferencia);

		DXCUtil.wait(5);
		this.write(fechaConsultaTP, fechaConsulta);
		Evidence.save("Consulta Planes institucionales: Total Participes");
		this.click(btnConsultar);
		do {
			DXCUtil.wait(1);
		} while (this.element(mensajeAlerta) == null);

		String msjAlerta = this.getText(mensajeAlerta);
		if (mensajeAlerta != null && !msjAlerta.contains("Su solicitud fue realizada. El archivo se está generando")) {
			this.mensajeError();
		}
		Evidence.save("Mensaje de descarga");
		Reporter.reportEvent(Reporter.MIC_INFO, msjAlerta);
		this.click(Descargar);
		DXCUtil.wait(5);
		DescargaPlanesInstitucionales();
		Evidence.save("Descargas");
		this.closeCurrentBrowser();
		SettingsRun.exitTestIteration();

	}

//	Metodo que valida si el certificado de planes institucionales cambia de estado a procesado, sino cambia de estado recarga la pagina y vuelve a validar si cambia de estado
	private void DescargaPlanesInstitucionales() throws Exception {
		int contadordescarga = 0;
		List<WebElement> tablaDescargas = null;

		do {

			tablaDescargas = this.findElements(estadoDescarga2);
			for (WebElement recoreDatos : tablaDescargas) {
				contadordescarga++;
				String tablaDescargasTxt = this.getText(recoreDatos);
				if (tablaDescargasTxt.contains("En Proceso") || tablaDescargasTxt.contains("Procesado")) {

					try {
						if (this.findElement(linkDescarga) != null) {

							this.findElement(linkDescarga).click();
							DXCUtil.wait(10);
							Reporter.reportEvent(Reporter.MIC_PASS,
									"Se descarga documentos en estado procesado, verificar en la carpeta de evidencias generadas "
											+ tablaDescargasTxt);

						}
					} catch (NoSuchElementException e) {
						Reporter.reportEvent(Reporter.MIC_INFO,
								"Se supera el tiempo de espera, no es posible realizar la descarga por favor validar en Tipo de consulta Descargas si se genero el documento "
										+ tablaDescargasTxt);
					}

				}  

			}
			DXCUtil.wait(15);
			this.refresh();

			if (contadordescarga < tablaDescargas.size() + 1 && this.element(linkDescarga) == null) {
				Reporter.reportEvent(Reporter.MIC_INFO, "----------------------------------------");
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se recarga la página y se valida nuevamente si cambia el estado de los documentos a Procesado");
			}

		} while (contadordescarga < tablaDescargas.size() + 1);
	}

	// Valida si el número de referencia suministrado en el archivo de lanzamiento
	// se encuentra en la lista del campo referencia

	private void validarReferencia(By locator, String numReferencia) throws Exception {

		WebElement referenciaElm = this.findElement(locator);
		Select select = new Select(referenciaElm);
		List<WebElement> opciones = select.getOptions();
		for (int i = 0; i < opciones.size(); i++) {

			if (opciones.get(i).getText().equals(numReferencia)) {
				opciones.get(i).click();
				Evidence.save("Referencia ingresada");
				break;

			}
			if (i + 1 >= opciones.size()) {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontró la referencia indicada en el archivo, verifique el archivo");
				SettingsRun.exitTestIteration();
			}

		}
	}

	// Método que permite seleccionar tipos de certificado dependiendo el tipo de
	// producto
	private void tipoCertificados() throws Exception {
		String tipoCertificado = SettingsRun.getTestData().getParameter("Tipo de Certificado").trim();
		// Busca el elemento
		WebElement labelTipoCert = null;
		while (tiempoTranscurrido < tiempoEspera) {
			try {
				labelTipoCert = this.findElement(By.xpath("//td/label[text()='" + tipoCertificado + "']"));

				if (labelTipoCert != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
				}
			}
		}
		// Si encuentra el elemento hace la siguiente función
		if (labelTipoCert != null) {
			Evidence.save("Evidencia certificados");
			WebElement inputTipoCert = labelTipoCert.findElement(By.xpath("../input"));
			if (!inputTipoCert.isSelected()) {
				inputTipoCert.click();
				this.mensajeError();
				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se seleccionó el tipo de certificado: [ " + tipoCertificado + " ].");
				Evidence.save("Evidencia certificados");
			} else {
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Ya se encuentra seleccionado el tipo de certificado: [ " + tipoCertificado + " ].");
			}
		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se encontró el elemento.");

		}
	}

	/**
	 * Es uno de los metodos principales, dependiendo el tipo de certificado podra
	 * identificar que proceso debe realizar, toda la información del excel sera
	 * usada para cada diferente caso, el switch sera el encargado de dirigir que
	 * caracteristicas debe llevar el certificado
	 * 
	 * @param tipoCertificado
	 * @throws Exception
	 */
	private void certificadoCaracteristicas(String tipoCertificado) throws Exception {

		// Variables que se usaran en los casos
		String saldo = SettingsRun.getTestData().getParameter("Saldo").trim();
		String idioma = SettingsRun.getTestData().getParameter("Idioma").trim();
		String para = SettingsRun.getTestData().getParameter("Para").trim();
		String solicitante = SettingsRun.getTestData().getParameter("Solicitante").trim();
		String producto = SettingsRun.getTestData().getParameter("Producto").trim();
		String numeroProducto = SettingsRun.getTestData().getParameter("Número de producto").trim();
		String anyoTributarioSlc = SettingsRun.getTestData().getParameter("Selección de año tributario").trim();
		String tipoProducto = SettingsRun.getTestData().getParameter("Tipo de producto").trim();

		DXCUtil.wait(5);
		switch (tipoCertificado) {
		// Esta opción abarca las caracteristicas principales para el certificado
		// "Certificación comercial de productos vigentes"
		case "Certificación comercial de productos vigentes":

			// Método para verificar mensajes de error en la pantalla
			this.mensajeError();
			Evidence.save("Evidencia certificados");

			// Método que permite seleccionar un producto para solicitar el certificado
			this.seleccionarProducto(producto, numeroProducto);

			Evidence.save("Evidencia certificados");

			// Mostrar o no mostrar el saldo en el certificado, es una de las
			// caracteristicas del certificado
			WebElement showSaldo = null;
			WebElement dontShowSaldo = null;
			while (tiempoTranscurrido < tiempoEspera) {
				// Busca el/los elementos
				try {
					showSaldo = this.findElement(mostrarSaldo);
					dontShowSaldo = this.findElement(noMostrarSaldo);

					if (showSaldo != null && dontShowSaldo != null) {
						break;
					}
				} catch (NoSuchElementException e) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
					}
				}
			}
			// Si encuentra el elemento hace la siguiente función, dependiendo la
			// información del excel
			if (showSaldo != null && dontShowSaldo != null) {
				this.mensajeError();
				if (saldo.equals("Con saldo") && showSaldo.isDisplayed()) {
					showSaldo.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "Se mostrara el saldo en el certificado.");
				} else if (saldo.equals("Sin saldo")) {
					dontShowSaldo.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "No se mostrara el saldo en el certificado.");
				}
			}
			Evidence.save("Evidencia certificados");

			/**
			 * En idioma español o inglés en el certificado, dependiendo el idioma se
			 * seleccionara automaticamente la divisa, por ejemplo: español = Peso ingles =
			 * dolar
			 */

			WebElement espanyol = null;
			WebElement ingles = null;
			while (tiempoTranscurrido < tiempoEspera) {
				// Busca el/los elementos

				try {
					espanyol = this.findElement(idiomaEspanyol);
					ingles = this.findElement(idiomaIngles);

					if (espanyol != null && ingles != null) {
						break;
					}
				} catch (NoSuchElementException e) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
					}
				}
			}
			// Si encuentran los elementos hace la siguiente función, dependiendo la
			// información del excel
			if (espanyol != null && ingles != null) {
				this.mensajeError();
				if (idioma.equals("En español")) {
					espanyol.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "El idioma del certificado es español.");
				} else if (idioma.equals("En inglés")) {
					ingles.click();

					Reporter.reportEvent(Reporter.MIC_PASS, "El idioma del certificado es en ingles.");
				}
			}
			Evidence.save("Evidencia certificados");

			// Método para saber a quién va dirigido el certificado
			this.aQuienInterese(para, solicitante);

			// Método que selecciona una ciudad
			this.seleccionarCiudad();
			Evidence.save("Evidencia certificados_2");
			break;

		// Estos casos usan el mismo método porque tienen simulitud en los casos
		case "Certificación saldo para productos de crédito":
			this.certSaldosyPazySalvos(producto, numeroProducto, para, solicitante);
			break;

		case "Paz y salvos productos de crédito cancelados":
			this.certSaldosyPazySalvos(producto, numeroProducto, para, solicitante);
			break;

		// Este caso puede llegar a variar mucho dependiendo el tipo de producto
		case "Certificaciones tributarias":
			this.mensajeError();
			String certifTribuHastaDesde = SettingsRun.getTestData()
					.getParameter("Certificaciones tributarias hasta / desde");

			WebElement desde = null;
			WebElement hasta = null;

			while (tiempoTranscurrido < tiempoEspera) {
				// estos campos en algunos casos puede que aparezcan o puede que no, ya depende
				// del producto para el certificado, de igual manera si en algun caso no se
				// llegan a encontrar, no contaran con esta validación
				try {
					desde = this.findElement(desde2018);
					hasta = this.findElement(hasta2017);

					if (desde != null && hasta != null) {
						break;
					}
				} catch (NoSuchElementException e) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos desde y hasta.");
					}
				}
			}
			// Si encuentran los elementos hace la siguiente función, dependiendo la
			// información del excel
			if (desde != null && hasta != null) {
				if ("hasta 2017".equals(certifTribuHastaDesde)) {
					hasta.click();
					this.mensajeError();
					Reporter.reportEvent(Reporter.MIC_PASS,
							"Se seleccionó Certificaciones tributarias: [ " + certifTribuHastaDesde + " ].");
					this.validarOpcionesLocator(anyoTributario);
					this.seleccionarProducto(producto, numeroProducto);
					// Método que permite seleccionar un producto para solicitar el certificado
					Evidence.save("Evidencia certificados");
					WebElement seleccionAnyo = this.findElement(anyoTributario);
					while (tiempoTranscurrido < tiempoEspera) {
						try {
							seleccionAnyo = this.findElement(anyoTributario);

							if (seleccionAnyo != null) {
								this.selectListItem(seleccionAnyo, anyoTributarioSlc);
								this.mensajeError();
								break;
							} else {
								Reporter.reportEvent(Reporter.MIC_INFO,
										"certificado unificado no se encuentra disponible.");
							}
						} catch (NoSuchElementException e) {
							try {
								DXCUtil.wait(1);
								tiempoTranscurrido++;
							} catch (Exception ex) {
								Reporter.reportEvent(Reporter.MIC_INFO,
										"No se encontraron los elementos desde y hasta.");
							}
						}
					}
					Evidence.save("Evidencia certificados");
				} else if ("desde 2018".equals(certifTribuHastaDesde) || tipoProducto.equals("Tipo de producto")
						&& tipoCertificado.equals("Certificaciones tributarias")) {
					desde.click();
					Reporter.reportEvent(Reporter.MIC_PASS,
							"Se seleccionó Certificaciones tributarias: [ " + certifTribuHastaDesde + " ].");
					Evidence.save("Evidencia certificados");
					this.mensajeError();
					this.validarOpcionesLocator(selectProducto);
					this.validarOpcionesLocator(anyoTributario);

					WebElement productUnificado = null;
					WebElement anioTributario = null;

					while (tiempoTranscurrido < tiempoEspera) {
						try {
							productUnificado = this.findElement(selectProducto);
							anioTributario = this.findElement(anyoTributario);

							if (productUnificado != null && anioTributario != null) {
								this.selectListItem(productUnificado, "Certificación unificada productos Davivienda");
								this.mensajeError();
								Reporter.reportEvent(Reporter.MIC_PASS,
										"Se seleccionó el prodcuto: Certificación unificada productos Davivienda");
								Evidence.save("Evidencia certificados");
								this.selectListItem(anioTributario, anyoTributarioSlc);
								this.mensajeError();
								Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó el año: " + anyoTributarioSlc);
								Evidence.save("Evidencia certificados");
								break;
							}
						} catch (NoSuchElementException e) {
							try {
								DXCUtil.wait(1);
								tiempoTranscurrido++;
							} catch (Exception ex) {
								Reporter.reportEvent(Reporter.MIC_INFO,
										"No se encontraron los elementos desde y hasta.");
							}
						}
					}
					Evidence.save("Evidencia certificados");
				}
			} else {
				Reporter.reportEvent(Reporter.MIC_INFO, "El certificado no contara con información desde o hasta");

				WebElement productoTrib = null;
				WebElement anioTributario = null;

				this.mensajeError();
				try {
					productoTrib = this.findElement(selectProducto);
					anioTributario = this.findElement(anyoTributario);

					if (productoTrib != null && anioTributario != null) {
						this.validarOpcionesWebElement(productoTrib);
						this.validarOpcionesWebElement(anioTributario);
						Reporter.reportEvent(Reporter.MIC_PASS,
								"Se seleccionó el prodcuto: Certificación unificada productos Davivienda");
						Evidence.save("Evidencia certificados");
						this.mensajeError();
						this.selectListItem(anioTributario, anyoTributarioSlc);
						Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó el año: " + anyoTributarioSlc);
						Evidence.save("Evidencia certificados");
						break;
					}
				} catch (NoSuchElementException e) {
					try {
						productoTrib = this.findElement(selectProducto);
						anioTributario = this.findElement(anyoTributario2);

						if (productoTrib != null && anioTributario != null) {
							this.validarOpcionesWebElement(productoTrib);
							this.validarOpcionesWebElement(anioTributario);
							this.pagePortalPymes.lisProducto(selectProducto, tipoProducto, numeroProducto);
							this.selectListItem(anioTributario, anyoTributarioSlc);
							break;
						}
					} catch (NoSuchElementException ex) {
						try {
							productoTrib = this.findElement(certificadoUnificado);
							anioTributario = this.findElement(anyoCertUnificado);

							if (productoTrib != null && anioTributario != null) {
								this.validarOpcionesWebElement(productoTrib);
								this.validarOpcionesWebElement(anioTributario);
								this.selectListItem(productoTrib, "Certificación unificada productos Davivienda");
								Reporter.reportEvent(Reporter.MIC_PASS,
										"Se seleccionó el prodcuto: Certificación unificada productos Davivienda");
								Evidence.save("Evidencia certificados");
								this.mensajeError();
								this.selectListItem(anioTributario, anyoTributarioSlc);
								Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó el año: " + anyoTributarioSlc);
								Evidence.save("Evidencia certificados");
								break;
							}
						} catch (NoSuchElementException err) {
							try {
								DXCUtil.wait(1);
								tiempoTranscurrido++;
							} catch (java.util.NoSuchElementException er) {
								Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
							}
						}
					}
				}
			}

			Evidence.save("Evidencia certificados");
			break;

		case "Reporte anual de costos":

			String reporteAnual = SettingsRun.getTestData().getParameter("Reporte Anual");
			this.mensajeError();
			Evidence.save("Evidencia certificados");
			// Valida si el selector contiene información para seleccionar
			this.validarOpcionesLocator(seleccionAnual);

			WebElement selectAnual = null;
			while (tiempoTranscurrido < tiempoEspera) {
				try {
					selectAnual = this.findElement(seleccionAnual);

					if (selectAnual != null) {
						// Si encuentran los elementos hace la siguiente función, dependiendo la
						// información del excel
						this.selectListItem(seleccionAnual, reporteAnual);
						this.mensajeError();
						break;
					} else {
						Reporter.reportEvent(Reporter.MIC_INFO, "certificado unificado no se encuentra disponible.");
					}
				} catch (NoSuchElementException e) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontró el elemento.");
					}
				}
			}

			Evidence.save("Evidencia certificados");

			break;

		// Este caso es unico, no tiene mucha familiaridad con los otros casos
		case "Certificaciones GMF":
			String tipoInversion = SettingsRun.getTestData().getParameter("Tipo Inversión").trim();
			String numeroInversion = SettingsRun.getTestData().getParameter("Número Inverción").trim();
			String banco = SettingsRun.getTestData().getParameter("Banco Inversión").trim();
			String tipoProductoCuenta = SettingsRun.getTestData().getParameter("Tipo de cuenta").trim();
			String numeroProductoCuenta = SettingsRun.getTestData().getParameter("Número producto").trim();

			this.mensajeError();

			Evidence.save("Evidencia certificados");
			WebElement fondInversion = null;
			WebElement bancoInversion = null;
			WebElement tipProductoInver = null;

			while (tiempoTranscurrido < tiempoEspera) {
				try {
					// Enviar información
					fondInversion = this.findElement(fondoInversionFIC);
					bancoInversion = this.findElement(bancoFIC);
					tipProductoInver = this.findElement(tipoProductoFIC);

					if (fondInversion != null && bancoInversion != null && tipProductoInver != null) {
						break;
					}
				} catch (NoSuchElementException e) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontró el elemento.");
					}
				}
			}

			// obtener la información de los inputs para validar la información con la
			// automatización
			if (fondInversion != null && bancoInversion != null && tipProductoInver != null) {
				this.pagePortalPymes = new PagePortalPymes(this);

				this.validarOpcionesWebElement(fondInversion);
				this.pagePortalPymes.lisProducto(fondoInversionFIC, tipoInversion, numeroInversion);
				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se ha seleccionado el fondo de inversión: [ " + tipoInversion + " " + numeroInversion + " ].");
				this.mensajeError();

				this.validarOpcionesLocator(bancoFIC);

				this.selectListItem(bancoFIC, banco);

				Evidence.save("Evidencia certificados GMF");
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha seleccionado el banco: [ " + banco + " ].");
				this.mensajeError();

				this.validarOpcionesLocator(tipoProductoFIC);
				this.selectListItem(tipoProductoFIC, tipoProductoCuenta);
				Evidence.save("Evidencia certificados GMF");
				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se ha seleccionado el tipo de cuenta: [ " + tipoProductoCuenta + " ].");
				this.mensajeError();

				this.findElement(numeroProductoFIC).sendKeys(numeroProductoCuenta);
				Evidence.save("Evidencia certificados GMF");
				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se ha ingresado el número de cuenta: [ " + numeroProductoCuenta + " ].");

				this.findElement(aceptResponsabilidad).click();
				Evidence.save("Evidencia certificados GMF");
				this.mensajeError();

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontraron los elementos para realizar la descarga de la certificación.");
				this.pagePortalPymes.terminarIteracion();
			}

			String informacion = this.findElement(informacionFIC).getText();
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se ha aceptado la siguiente información: [ " + informacion + " ].");

			break;

		default:
			break;
		}
	}

	/**
	 * Método que permite validar si salen mensajes de error cuando se seleccionen
	 * opciones y carga nueva información en la pantalla
	 * 
	 * @throws Exception
	 */
	private void mensajeError() throws Exception {
		this.pagePortalPymes = new PagePortalPymes(this);
		DXCUtil.wait(5);
		String mensaje = this.pagePortalPymes.getMsgAlertIfExist("lblMasterAlerta");
		if (mensaje != null) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Se visualizo el siguiente mensaje informativo: " + mensaje);
			Evidence.save("Evidencia certificados");
			this.pagePortalPymes.terminarIteracion();
		}
	}

	/**
	 * Valida las opciones de un select por medio de un locator
	 * 
	 * @param elemento
	 * @throws Exception
	 */
	private void validarOpcionesLocator(By locator) throws Exception {
		this.pagePortalPymes = new PagePortalPymes(this);
		this.mensajeError();
		WebElement elemento = null;
		while (tiempoTranscurrido < tiempoEspera) {
			try {
				elemento = this.findElement(locator);

				if (elemento != null) {
					List<WebElement> elementos = elemento.findElements(By.tagName("option"));

					if (!elementos.isEmpty()) {
						Reporter.reportEvent(Reporter.MIC_PASS, "Se encontraron opciones para seleccionar.");
						DXCUtil.wait(3);
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"El elemento " + elemento + " no contiene opciones disponibles");
						Evidence.save("Evidencia certificados");
						this.pagePortalPymes.terminarIteracion();
					}
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontraron los elementos.");
				}
			}
		}
	}

	/**
	 * Valida las opciones de un select por medio de un WebElement
	 * 
	 * @param elemento
	 * @throws Exception
	 */

	private void validarOpcionesWebElement(WebElement elemento) throws Exception {
		this.pagePortalPymes = new PagePortalPymes(this);
		this.mensajeError();
		while (tiempoTranscurrido < tiempoEspera) {
			try {
				if (elemento != null) {
					List<WebElement> elementos = elemento.findElements(By.tagName("option"));

					if (!elementos.isEmpty()) {
						Reporter.reportEvent(Reporter.MIC_PASS, "Se encontraron opciones para seleccionar.");
						DXCUtil.wait(3);
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"El elemento " + elemento + " no contiene opciones disponibles");
						Evidence.save("Evidencia certificados");
						this.pagePortalPymes.terminarIteracion();
					}
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontraron los elementos.");
				}
			}
		}
	}

	/**
	 * Método que permite seleccionar un producto para el certificado
	 * 
	 * @param producto
	 * @param numeroProducto
	 * @throws Exception
	 */

	private void seleccionarProducto(String producto, String numeroProducto) throws Exception {

		this.pagePortalPymes = new PagePortalPymes(this);
		this.mensajeError();

		int tiempoEspera = 5;
		int tiempoTranscurrido = 0;

		WebElement slctProduct = null;

		while (tiempoTranscurrido < tiempoEspera) {
			// Buscar el elmento
			try {
				slctProduct = this.findElement(selectProducto);

				if (slctProduct != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontraron los elementos.");
				}
			}
		}
		// una vez encuentre el elemento, se seleccionara la opción indicada en la data
		if (slctProduct != null) {
			this.validarOpcionesWebElement(slctProduct);

			if (!producto.isEmpty() && !numeroProducto.isEmpty()) {
				this.pagePortalPymes.lisProducto(selectProducto, producto, numeroProducto);
				DXCUtil.wait(5);
				Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó el producto: [ " + producto
						+ " ] y el número de producto: [ " + numeroProducto + " ].");
				Evidence.save("Evidencia certificados");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"En el excel no se encontro el producto o el número de producto");
				Evidence.save("Evidencia certificados");
				this.pagePortalPymes.terminarIteracion();
			}
		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "El certificado no cuenta con producto.");
			Evidence.save("Evidencia certificados");
		}

	}

	/**
	 * Metodo que permite buscar y seleccionar la ciudad de emisión del certificado,
	 * seleccionando la opción aleatoriamente.
	 * 
	 * @throws Exception
	 */

	private void seleccionarCiudad() throws Exception {
		// Valida si el selector contiene información para seleccionar
		this.pagePortalPymes = new PagePortalPymes(this);
		this.mensajeError();
		this.validarOpcionesLocator(ciudadEmision);
		WebElement seleccionarCiudad = null;

		while (tiempoTranscurrido < tiempoEspera) {
			try {
				seleccionarCiudad = this.findElement(ciudadEmision);

				if (seleccionarCiudad != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					DXCUtil.wait(1);
					tiempoTranscurrido++;
				} catch (Exception ex) {
					Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos");
				}
			}
		}
		Evidence.save("Evidencia certificados");
		if (seleccionarCiudad != null) {
			// Selecciona una ciudad random para validar de donde se solicita
			List<WebElement> ciudades = seleccionarCiudad.findElements(By.tagName("option"));
			Random random = new Random();
			int index = random.nextInt(ciudades.size());
			WebElement ciudadRandom = ciudades.get(index);
			Reporter.reportEvent(Reporter.MIC_PASS,
					"Se seleccionó la ciudad de emición: [ " + ciudadRandom.getText() + " ].");
			ciudadRandom.click();
			Evidence.save("Evidencia certificados");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"El select [ " + seleccionarCiudad + " ] no contiene opciones disponibles.");
			Evidence.save("Evidencia certificados");
			this.pagePortalPymes.terminarIteracion();
		}

	}

	/**
	 * Las certificaciones pueden variar dependiendo el producto o tipo de
	 * certificado, para este caso los tipos de certificado: "Certificación saldo
	 * para productos de crédito" y "Paz y salvos productos de crédito cancelados",
	 * estos casos solicitan la misma información y los locators no van a variar.
	 * 
	 * @param producto
	 * @param numeroProducto
	 * @param para
	 * @param solicitante
	 * @throws Exception
	 */
	private void certSaldosyPazySalvos(String producto, String numeroProducto, String para, String solicitante)
			throws Exception {
		this.mensajeError();
		// Método que permite seleccionar un producto para solicitar el certificado
		this.seleccionarProducto(producto, numeroProducto);

		Evidence.save("Evidencia certificados");
		this.aQuienInterese(para, solicitante);
		this.seleccionarCiudad();
		Evidence.save("Evidencia certificados");
	}

	/**
	 * Método encargado de seleccionar y diligenciar para quién va el certificado y
	 * si es el caso el solicitante del certificado.
	 * 
	 * @param para
	 * @param solicitante
	 * @throws Exception
	 */
	private void aQuienInterese(String para, String solicitante) throws Exception {

		Evidence.save("Evidencia certificados");
		// Estos elementos dependiendo la pantalla
		// Pueden cambiar por eso se encuentran nulos
		WebElement quienInterese = null;
		WebElement dirigido = null;

		// Este while permite dar un tiempo de espera para encontrar el elemento va a
		// ser usado
		while (tiempoTranscurrido < tiempoEspera) {
			// El trycatch permite encontrar los elementos, si encuentra los elementos el
			// break finalizara la busqueda dependiendo
			// a que try entre a encontrar los elementos
			try {
				quienInterese = this.findElement(aQuienInterese);
				dirigido = this.findElement(dirigidoA);

				if (quienInterese != null && dirigido != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				try {
					quienInterese = this.findElement(aQuienIntereseComercial);
					dirigido = this.findElement(dirigidoAComercial);

					if (quienInterese != null && dirigido != null) {
						break;
					}

				} catch (Exception e2) {
					try {
						DXCUtil.wait(1);
						tiempoTranscurrido++;
					} catch (Exception ex) {
						Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron los elementos.");
					}
				}
			}
		}
		DXCUtil.wait(4);
		// Si los elementos no son nulos procede a realizar la validación dependiendo la
		// información extraida del excel frente a este caso
		if (quienInterese != null && dirigido != null) {
			// Valida a quién va dirigido
			if (para.equals("A quien interese")) {
				quienInterese.click();
				this.mensajeError();
				Reporter.reportEvent(Reporter.MIC_PASS, "El certificado va a quien interese");
			} else if (para.equals("Dirigido a:")) {
				dirigido.click();
				Reporter.reportEvent(Reporter.MIC_PASS, "El certificado va a ser dirigido a alguien.");
				this.mensajeError();
				// Verifica cual locator se encuentra presente
				WebElement paraElm = null;
				try {
					// Si encuentra este elemento.
					paraElm = this.findElement(envioParaComercial);
				} catch (Exception e) {
					try {
						// Si encuentra este elemento.
						paraElm = this.findElement(envioPara);
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}

				// Si encuentra este elemento, enviara la información correspondiente.
				paraElm.sendKeys(solicitante);
				Reporter.reportEvent(Reporter.MIC_PASS, "Se asigno el nombre de solicitante: [ " + solicitante + " ].");
			}
		}
		DXCUtil.wait(3);

	}

}
