package dav.Autoservicio;

import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dav.AutogestionServicios.PageAutogestionServicioMiddle;
import dav.pymes.PageLoginPymes;
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

public class PageAutoservicio extends BasePageWeb {

	public PageAutoservicio(BasePageWeb parentPage) {
		super(parentPage);
	}

	// =======================================================================================================================
	// Page & Crontroller
	PageOrigen pageOrigen = null;
	PageAutogestionServicioMiddle pagePageAutogestionServicioMiddle = null;
	PageLoginPymes pageLogin = null;
	PageConfirmacion pageConfirmacion = null;

	// Locators botones Activar Token
	By atTipoDocumento = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:tiposDocumento");
	By atNumeroDocumento = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:numeroDocumento");
	By atNumClienteEmps = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:clienteEmpresarial");
	By atCodigoActivacion = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:codigoActivacion");
	By atNumSerialToken = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:numeroSerial");
	// Locators Autenticar
	By aTipoDocumento = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:tiposDocs");
	By aNumeroDocumento = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:numeroDocumento");
	By aNumClienteEmps = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:clienteEmpresarial");
	By aClaveActual = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:claveActual");
	By aClvDinmToken = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:claveToken");

	By btnAutCancelar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_1f");
	By btnAutContinuar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_1g");

	// Botones activar token
	By btnAtCancelar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_11");
	By btnAtContinuar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_12");

	// Locators Asignar clave personal
	By acpNuevaClvPersonal = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:nuevaClavePersonal");
	By acpConfClvPersonal = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:confirmarClavePersonal");
	By acpClvDinmToken = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:claveToken");
	// Botones asignar clave personal
	By btnAcpCancelar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_16");
	By btnAcpContinuar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_17");

	// Locators configurar nuevas preguntas y respuestas
	By selectPregunta1 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:pregunta1");
	By respuesta1 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:respuesta1");

	By selectPregunta2 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:pregunta2");
	By respuesta2 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:respuesta2");

	By selectPregunta3 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:pregunta3");
	By respuesta3 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:respuesta3");

	By selectPregunta4 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:pregunta4");
	By respuesta4 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:respuesta4");

	By selectPregunta5 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:pregunta5");
	By respuesta5 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:respuesta5");
	// Botones configurar preguntas
	By btnCpCancelar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:j_id_1r");
	By btnCpContinuar = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formconfpreguntas:j_id_1s");

	// Locators Confirmar
	By btnConfirmarActivacion = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:j_id_7:j_id_e");

	// Locators Cambio de clave por olvido
	By tipDocumentoOCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:tiposDocs");
	By numeroDocumentoOCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:numeroDocumento");
	By numeroClienteEmpOCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:clienteEmpresarial");

	By btnContinuarOCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:consultar");

	// Locator tabla con preguntas y respuestas
	By preguntasRespuestasOlvidoClv = By.xpath(
			"//*[@id='viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:panelPreguntasRespuestas']/table/tbody");

	// Preguntas y respuestas de seguridad para cambio de clave
	By respuestaOCP1 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:respuesta1");
	By respuestaOCP2 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:respuesta2");
	By respuestaOCP3 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:respuesta3");
	By respuestaOCP4 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:respuesta4");
	By respuestaOCP5 = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:respuesta5");

	By btnCancelarPrCCPO = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:cancelar");
	By btnContinuarPrCCPO = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:validar");

	By nuevaClaveNCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:nuevaClavePersonal");
	By confClaveNCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:confirmarClavePersonal");

	By btnCancelarNCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_1f");
	By btnContinuarNCP = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:j_id_1g");

	By btnContinuarNC = By.id("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:j_id_7:j_id_e");

	// Locators Cambio de clave personal

	// ------------------------------------ Locators Login Front
	By inpNumeroCliente = By.xpath("//*[@id='divNumerClienteEmpresarial']/input");
	By selTipoIdentificacion = By.xpath("//*[@id='divTipoIdentificacion']/select");
	By inpNumeroDocumento = By.xpath("//*[@id='divNumeroDocumento']/input");
	By btnIngresarFront = By.id("btnIngresar");

	String activToken = null;

	PagePortalPymes pagePortalPymes = null;

	/*
	 * // Método encargado a redirigir el tipo de prueba seleccionado en el excel
	 * 
	 */

	public void autoservicio() throws Exception {
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo Prueba");

		switch (tipoPrueba) {

		case "Activar Token":
			this.activarToken();
			break;

		case "Cambiar clave":
			this.cambioClave();
			break;

		case "Cambiar clave por olvido":
			this.cambioClaveOlvido();
			break;

		}
	}

	/*
	 * // Método para relizar la activación de token
	 * 
	 */

	public void activarToken() throws Exception {

		String portalAutoservicio = SettingsRun.getTestData().getParameter("Portal Autoservicios").trim();
		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String numeroClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String codigoActivacion = SettingsRun.getTestData().getParameter("Código de Activación").trim();
		String serialToken = SettingsRun.getTestData().getParameter("Número Serial Token").trim();

		// Preguntas
		String preguntaCp1 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 1").trim();
		String preguntaCp2 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 2").trim();
		String preguntaCp3 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 3").trim();
		String preguntaCp4 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 4").trim();
		String preguntaCp5 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 5").trim();
		// Respuestas
		String respuestaCp1 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 1").trim();
		String respuestaCp2 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 2").trim();
		String respuestaCp3 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 3").trim();
		String respuestaCp4 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 4").trim();
		String respuestaCp5 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 5").trim();

		/*
		 * // Primera pantalla, Activar Token
		 * 
		 */

		// Permite validar si en que portal se enuentra para realizar la acción de
		// activación de token
		if (portalAutoservicio.equals("FRONT")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Front.");
			DXCUtil.wait(5);
			this.ingresoFront();
			DXCUtil.wait(3);
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("frameAUT");
			// Permitira dar click al elemento del frame que activa el token
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(1) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		} else if (portalAutoservicio.equals("MIDDLE")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Middle.");
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("iFrameAut");
			// Permitira dar click al elemento del frame que activa el token
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(1) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		}
		DXCUtil.wait(5);
		// Permite interactuar con el cambio de url que trae nuevos elementos, sin este
		// No se podria interactuar con la página y no continuaria la automatización
		this.getDriver().switchTo().activeElement();

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se registraran los siguientes datos: [ " + tipoIdentificacion + " - " + numeroClienteEmpresa + " - "
						+ numeroClienteEmpresa + " - " + codigoActivacion + " - " + serialToken + " ]");

		/*
		 * // Segunda pantalla, Activar token
		 * 
		 */
		DXCUtil.wait(5);
		this.selectListItem(atTipoDocumento, tipoIdentificacion);
		// ingreso de data
		By[] fieldsAt = { atNumeroDocumento, atNumClienteEmps, atCodigoActivacion, atNumSerialToken };
		String[] dataAt = { numeroDocumento, numeroClienteEmpresa, codigoActivacion, serialToken };

		// Método que permite ingresar la información a los campos
		ingresarDatos(getDriver(), fieldsAt, dataAt);
		Evidence.save("Evidencias activación de token");
		DXCUtil.wait(5);
		// Continua el siguiente paso
		click(this.findElement(btnAtContinuar));
		DXCUtil.wait(2);
		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		DXCUtil.wait(5);
		Reporter.reportEvent(Reporter.MIC_PASS, "Se activo ingresó la información correctamente.");

		/*
		 * // Tercer pantalla, Asignar clave personal
		 * 
		 */
		this.activarDinamica();
		/*
		 * // Cuarta pantalla, Preguntas y respuestas
		 * 
		 */

		// Ingresa las respuestas para seguridad en los locators
		DXCUtil.wait(4);
		this.selectListItem(selectPregunta1, preguntaCp1);
		this.selectListItem(selectPregunta2, preguntaCp2);
		this.selectListItem(selectPregunta3, preguntaCp3);
		this.selectListItem(selectPregunta4, preguntaCp4);
		this.selectListItem(selectPregunta5, preguntaCp5);
		Evidence.save("Evidencias activación de token");

		DXCUtil.wait(2);

		By[] fieldsCp = { respuesta1, respuesta2, respuesta3, respuesta4, respuesta5 };
		String[] dataCp = { respuestaCp1, respuestaCp2, respuestaCp3, respuestaCp4, respuestaCp5 };

		// Ingresa las respuestas en cada locator
		ingresarDatos(getDriver(), fieldsCp, dataCp);
		Evidence.save("Evidencias activación de token");
		DXCUtil.wait(2);
		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se guardaron las siguientes preguntas y respuestas de seguridad: " + "\nPregunta 1: " + preguntaCp1
						+ " = Respuesta 1: " + respuestaCp1 + "\nPregunta 2: " + preguntaCp2 + " = Respuesta 2: "
						+ respuestaCp2 + "\nPregunta 3: " + preguntaCp3 + " = Respuesta 3: " + respuestaCp3
						+ "\nPregunta 4: " + preguntaCp4 + " = Respuesta 4: " + respuestaCp4 + "\nPregunta 5: "
						+ preguntaCp5 + " = Respuesta 5: " + respuestaCp5 + "\n Mantenga esta información almacenada.");

		DXCUtil.wait(5);

		// Continuar para completar el registro
		click(this.findElement(btnCpContinuar));
		Reporter.reportEvent(Reporter.MIC_PASS, "Se han ingresado las preguntas de seguridad correctamente.");

		/*
		 * // Cuarta pantalla, clave personal Asignada
		 * 
		 */
		DXCUtil.wait(2);
		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		// Confirmar que se ha creado exitosamente
		click(this.findElement(btnConfirmarActivacion));
		DXCUtil.wait(2);
		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		Evidence.save("Evidencias activación de token");
		Reporter.reportEvent(Reporter.MIC_PASS, "Se asigno token correctamente.");
	}

	/*
	 * // Método que permite realizar el cambio de clave
	 * 
	 */
	public void cambioClave() throws Exception {

		String portalAutoservicio = SettingsRun.getTestData().getParameter("Portal Autoservicios").trim();
		String nuevaClave = SettingsRun.getTestData().getParameter("Nueva Clave / Cambio Clave Personal o Olvido")
				.trim();

		/*
		 * // Primera pantalla, Autenticar
		 * 
		 */

		// Permite validar si en que portal se enuentra para realizar la acción de
		// activación de token
		if (portalAutoservicio.equals("FRONT")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Front.");
			DXCUtil.wait(5);
			this.ingresoFront();
			DXCUtil.wait(3);
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("frameAUT");
			// Permitira dar click al elemento del frame que activa el token
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(4) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		} else if (portalAutoservicio.equals("MIDDLE")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Middle.");
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("iFrameAut");
			// Permitira dar click al elemento del frame que activa el token
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(4) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		}

		// Permite interactuar con el cambio de url que trae nuevos elementos, sin este
		// No se podria interactuar con la página y no continuaria la automatización
		this.getDriver().switchTo().activeElement();
		DXCUtil.wait(5);

		/*
		 * // Segunda pantalla, Cambiar clave personal
		 * 
		 */

		this.claveDinamicaPorCambioClave();

		DXCUtil.wait(2);
		String[] dataNCP = { nuevaClave, nuevaClave };
		By[] fieldsNCP = { nuevaClaveNCP, confClaveNCP };

		this.ingresarDatos(getDriver(), fieldsNCP, dataNCP);
		Evidence.save("Evidencias asignación de clave");
		click(this.findElement(btnContinuarNCP));
		this.mensajeInfo();

		if (this.element(btnAutContinuar) != null) {
			this.findElement(btnAutContinuar).click();
		}
		this.mensajeInfo();

		/*
		 * // Tercer pantalla, clave personal Asignada
		 * 
		 */
		DXCUtil.wait(2);

		// Confirmar que se ha creado exitosamente
		click(this.findElement(btnConfirmarActivacion));
		this.mensajeInfo();
		DXCUtil.wait(2);

		Evidence.save("Evidencias activación de token");
		Reporter.reportEvent(Reporter.MIC_PASS, "Se asigno cambio la clave correctamente.");

	}

	/*
	 * // Método que permite realizar el cambio de clave por olvido
	 * 
	 */

	public void cambioClaveOlvido() throws Exception {

		String portalAutoservicio = SettingsRun.getTestData().getParameter("Portal Autoservicios").trim();
		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String numeroClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();

		String preguntaOCP1 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 1").trim();
		String preguntaOCP2 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 2").trim();
		String preguntaOCP3 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 3").trim();
		String preguntaOCP4 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 4").trim();
		String preguntaOCP5 = SettingsRun.getTestData().getParameter("Pregunta de Seguridad 5").trim();

		String respuestaOCP1 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 1").trim();
		String respuestaOCP2 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 2").trim();
		String respuestaOCP3 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 3").trim();
		String respuestaOCP4 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 4").trim();
		String respuestaOCP5 = SettingsRun.getTestData().getParameter("Respuesta de Seguridad 5").trim();

		String nuevaClave = SettingsRun.getTestData().getParameter("Nueva Clave / Cambio Clave Personal o Olvido")
				.trim();
		/*
		 * // Primera pantalla, Cambiar clave personal
		 * 
		 */

		// Permite validar si en que portal se enuentra para realizar la acción de
		// Cambio de clave personal
		DXCUtil.wait(2);
		if (portalAutoservicio.equals("FRONT")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Front.");
			DXCUtil.wait(5);
			this.ingresoFront();
			DXCUtil.wait(3);
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("frameAUT");
			// Permitira dar click al elemento del frame que cambia la clave
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(5) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		} else if (portalAutoservicio.equals("MIDDLE")) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Entro por Middle.");
			// Nos permite entrar al frame en la pagina de login
			this.getDriver().switchTo().frame("iFrameAut");
			// Permitira dar click al elemento del frame que cambia la clave
			String scriptReloadPage = "document.querySelector('#collapseExample > ul > li:nth-child(5) > a').click()";
			this.getJse().executeScript(scriptReloadPage);
		}

		// Permite interactuar con el cambio de url que trae nuevos elementos, sin este
		// No se podria interactuar con la página y no continuaria la automatización
		this.getDriver().switchTo().activeElement();
		DXCUtil.wait(5);

		/*
		 * // Segunda pantalla, Cambiar clave personal
		 * 
		 */

		this.selectListItem(tipDocumentoOCP, tipoIdentificacion);

		String[] data = { numeroDocumento, numeroClienteEmpresa };
		By[] fields = { numeroDocumentoOCP, numeroClienteEmpOCP };

		DXCUtil.wait(1);
		this.ingresarDatos(getDriver(), fields, data);
		Evidence.save("Evidencias cambio de clave por olvido.");
		if (this.element(btnContinuarOCP) != null) {
			this.findElement(btnContinuarOCP).click();
		}

		/*
		 * // Tercera pantalla, Cambiar clave personal
		 * 
		 */
		DXCUtil.wait(2);
		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		// Array de preguntas y respuestas
		List<String> preguntas = Arrays.asList(preguntaOCP1, preguntaOCP2, preguntaOCP3, preguntaOCP4, preguntaOCP5);
		List<String> respuestas = Arrays.asList(respuestaOCP1, respuestaOCP2, respuestaOCP3, respuestaOCP4,
				respuestaOCP5);
		WebElement preguntaElemento = null;
		// Recorre la cantidad de preguntas
		for (int i = 0; i < preguntas.size(); i++) {
			DXCUtil.wait(1);
			String pregunta = preguntas.get(i);
			String respuesta = respuestas.get(i);

			// Locator identificar pregunta
			try {
				preguntaElemento = this.findElement(By.xpath(
						"//*[@id='viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formcambiarclave:panelPreguntasRespuestas']/table/tbody/tr/td/div/table/tbody/tr/td/label[contains(text(),'"
								+ pregunta + "')]"));
				// Si encuentra el locator, el siguiente elemento debe implementar si contiene
				// un input
				WebElement respuestaElemento = preguntaElemento.findElement(By.xpath("../../td/input"));

				respuestaElemento.sendKeys(respuesta);
			} catch (Exception e) {
				SettingsRun.exitTestIteration();
				this.getDriver().close();
			}

			// una vez identificada la pregunta se procede a ingresar el inpur encontrado
			// para la pregunta
		}
		Evidence.save("Evidencias respuesta a preguntas.");
		DXCUtil.wait(1);
		click(this.findElement(btnContinuarPrCCPO));

		/*
		 * // Cuarta pantalla, Cambiar clave personal
		 * 
		 */

		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		String[] dataNCP = { nuevaClave, nuevaClave };
		By[] fieldsNCP = { nuevaClaveNCP, confClaveNCP };
		DXCUtil.wait(1);

		this.ingresarDatos(getDriver(), fieldsNCP, dataNCP);
		Evidence.save("Evidencias asignación de clave");
		click(this.findElement(btnContinuarNCP));
		// Método que permite validar el mensaje del resultado
		this.mensajeInfo();

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se realizo el cambio de clave con la siguiente información: [ Número documento: " + numeroDocumento
						+ " - Cliente empresa: " + numeroClienteEmpresa + " - Nueva clave: " + nuevaClave + " ]");

		DXCUtil.wait(2);
		Evidence.save("Evidencias cambio clave exitoso");

		/*
		 * // Quinta pantalla, Cambiar clave personal
		 * 
		 */

		click(this.findElement(btnContinuarNC));

		Reporter.reportEvent(Reporter.MIC_PASS, "Se cambio clave correctamente.");

	}

	// Método que permite ingresar la información en los campos
	private static void ingresarDatos(WebDriver driver, By[] fields, String[] data) throws Exception {
		if (fields.length != data.length) {
			Reporter.reportEvent(Reporter.MIC_WARNING, "La cantidad de campos y datos no coinciden");
			return;
		}

		for (int i = 0; i < fields.length; i++) {

			WebElement field = driver.findElement(fields[i]);
			if (field.isDisplayed()) {

				field.clear();
				DXCUtil.wait(2);
				field.sendKeys(data[i]);
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontraró el campo: " + field + ", por favor verifique la información.");
				SettingsRun.exitTestIteration();
			}
		}
	}

	// Método que permite ingresar datos a la pagina de login en front
	private void ingresoFront() throws Exception {
		// Primera pantalla, Activar Token
		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación");
		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario");
		String numeroClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");

		this.selectListItem(this.findElement(selTipoIdentificacion), tipoIdentificacion);

		String[] dataFront = { numeroClienteEmpresa, numeroDocumento };
		By[] fieldsFront = { inpNumeroCliente, inpNumeroDocumento };
		ingresarDatos(getDriver(), fieldsFront, dataFront);
		Evidence.save("Evidencias activación de token");

		DXCUtil.wait(2);
		click(this.findElement(btnIngresarFront));

	}

	// Método que valida el mensaje luego de pasar cada proceso
	private void mensajeInfo() throws Exception {

		this.pagePortalPymes = new PagePortalPymes(this);
		String mensajePage = this.pagePortalPymes
				.getMsgAlertIfExist("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:mensajesPage");
		String mensajeErrores = this.pagePortalPymes.getMsgAlertIfExist("divErrores");

		if (mensajePage != null) {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"Se visualizo el siguiente mensaje informativo: \n" + mensajePage);
			Evidence.saveFullPage("Mensaje Alerta", this);
			SettingsRun.exitTestIteration();
			this.getDriver().close();
		} else if (mensajeErrores != null) {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"Se visualizo el siguiente mensaje informativo: \n" + mensajeErrores);
			Evidence.saveFullPage("Mensaje Alerta", this);
			SettingsRun.exitTestIteration();
			this.getDriver().close();
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "Los campos se llenaron correctamente");
		}

	}

	private void activarDinamica() throws Exception {

		boolean continuar = false;
		int intentos = 0;
		// Este panel permite ingresar el token dinamico, almacenandolo para ser
		// registrado

		do {
			intentos++;
			Reporter.reportEvent(Reporter.MIC_INFO, "Intento de ingreso de clave dinamica numero: " + intentos);
			String claveDinamica = JOptionPane.showInputDialog(null,
					"Ingrese el token dinamico, por favor \n verifique que la carga del token este completa.",
					this.activToken);
			DXCUtil.wait(2);
			String nuevaClave = SettingsRun.getTestData()
					.getParameter("Nueva Clave / Cambio Clave Personal o Olvido");
			Reporter.reportEvent(Reporter.MIC_INFO, "Se registraran los siguientes datos: [ nueva clave: " + nuevaClave
					+ " - clave dinamica ingredasa: " + claveDinamica + " ]");

			By[] fieldsAcp = { acpNuevaClvPersonal, acpConfClvPersonal, acpClvDinmToken };
			String[] dataAcp = { nuevaClave, nuevaClave, claveDinamica };

			ingresarDatos(getDriver(), fieldsAcp, dataAcp);
			Evidence.save("Evidencias activación de token");
			DXCUtil.wait(5);

			click(this.findElement(btnAcpContinuar));
			Reporter.reportEvent(Reporter.MIC_PASS, "Se asigno clave correctamente..");

			this.pagePortalPymes = new PagePortalPymes(this);
			String mensajePage = this.pagePortalPymes
					.getMsgAlertIfExist("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:mensajesPage");
			String mensajeErrores = this.pagePortalPymes.getMsgAlertIfExist("divErrores");

			if (mensajePage != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Se visualizo el siguiente mensaje: \n" + mensajePage);
				Evidence.saveFullPage("Mensaje error", this);
				continuar = false;
			} else if (mensajeErrores != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Se visualizo el siguiente mensaje: \n" + mensajeErrores);
				Evidence.saveFullPage("Mensaje error", this);
				continuar = false;
			} else {
				continuar = true;
				Reporter.reportEvent(Reporter.MIC_PASS, "Los campos se llenaron correctamente");
			}
		} while (!continuar && intentos < 3);

		if (continuar) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Continuara con las siguientes pantallas");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Se agotaron los 3 intentos");
			SettingsRun.exitTestIteration();
			this.getDriver().close();
		}

	}

	private void claveDinamicaPorCambioClave() throws Exception {

		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String numeroClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String nuevaClave = SettingsRun.getTestData().getParameter("Nueva Clave / Cambio Clave Personal o Olvido")
				.trim();
		boolean continuar = false;
		int intentos = 0;
		do {
			intentos++;
			Reporter.reportEvent(Reporter.MIC_INFO, "Intento de ingreso de clave dinamica numero: " + intentos);

			this.selectListItem(aTipoDocumento, tipoIdentificacion);

			// Este panel permite ingresar el token dinamico, almacenandolo para ser
			// registrado
			String claveDinamica = JOptionPane.showInputDialog(null,
					"Ingrese el token dinamico, por favor \n verifique que la carga del token este completa, para evitar errores.",
					this.activToken);
			DXCUtil.wait(2);
			Reporter.reportEvent(Reporter.MIC_INFO, "Se registraran los siguientes datos: [ nueva clave: " + nuevaClave
					+ " - clave dinamica ingredasa: " + claveDinamica + " ]");

			String[] data = { numeroDocumento, numeroClienteEmpresa, nuevaClave, claveDinamica };
			By[] fields = { aNumeroDocumento, aNumClienteEmps, aClaveActual, aClvDinmToken };

			DXCUtil.wait(1);
			this.ingresarDatos(getDriver(), fields, data);
			Evidence.save("Evidencias cambio de clave.");
			if (this.element(btnAutContinuar) != null) {
				this.findElement(btnAutContinuar).click();
			}
			DXCUtil.wait(2);

			this.pagePortalPymes = new PagePortalPymes(this);
			String mensajePage = this.pagePortalPymes
					.getMsgAlertIfExist("viewns_Z7_H29IG282NO55A0QS3QVFNM0003_:formulario:mensajesPage");
			String mensajeErrores = this.pagePortalPymes.getMsgAlertIfExist("divErrores");

			if (mensajePage != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Se visualizo el siguiente mensaje: \n" + mensajePage);
				Evidence.saveFullPage("Mensaje error", this);
				continuar = false;
			} else if (mensajeErrores != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, "Se visualizo el siguiente mensaje: \n" + mensajeErrores);
				Evidence.saveFullPage("Mensaje error", this);
				continuar = false;
			} else {
				continuar = true;
				Reporter.reportEvent(Reporter.MIC_PASS, "Los campos se llenaron correctamente");
			}
		} while (!continuar && intentos < 3);

		if (continuar) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Continuara con las siguientes pantallas");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Se agotaron los 3 intentos");
			SettingsRun.exitTestIteration();
			this.getDriver().close();
		}

	}

}
