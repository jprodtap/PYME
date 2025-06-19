package dav.inscripciones;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.*;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.transversal.NotificacionSMS;

public class PageInscripcionesCuenta extends BasePageWeb {

	public PageInscripcionesCuenta(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

// =======================================================================================================================
	// Page & Crontroller
	PageOrigen pageOrigen = null;
	PageConfirmacion pageConfirmacion = null;
	NotificacionSMS notificacionesSMS = null;
	PagePortalPymes pagePortalPymes = null;

// -----------------------------------------------------------------------------------------------------------------------
	// LOCATORS DE LA RUTA
//	By pagosTransferenciasOtros = By.id("mnMenun2/table/tbody/tr/td[1]/a");
//	By inscripcion = By.id("mnMenun17/td/table/tbody/tr/td/a");
	By titulo = By.id("lblMasterTitulo");
// -----------------------------------------------------------------------------------------------------------------------
	// LOCATORS PARA COSULTAR UNA INSCRIPCIÓN
	By selectBanco = By.id("cphCuerpo_ddlBanco");
	By numeroProductDestino = By.id("cphCuerpo_txtNumeroDestino");
	By nombTitular = By.id("cphCuerpo_txtNombreTitular");
	By identificTitular = By.id("cphCuerpo_txtIdenticacionTitular");
	By btnBuscar = By.id("cphCuerpo_btnBuscar");

// -----------------------------------------------------------------------------------------------------------------------
	// LOCATORS PARA REALIZAR SELECCIONAR UN REGISTRO y modificarlo
	String selectionOption = "//*[@id='cphCuerpo_gvCuentasInscritas']/tbody/tr/td[contains(text(),'TITULAR')]/../td[contains(text(),'NUMERO_IDENTIFICACION')]/../td/center/a[contains(text(),'NUMERO_PRODUCTO')]/../../../td/input[@type='checkbox']";
	String selectionOptionEstado = "//*[@id='cphCuerpo_gvCuentasInscritas']/tbody/tr/td[contains(text(),'TITULAR')]/../td[contains(text(),'NUMERO_IDENTIFICACION')]/../td/center/a[contains(text(),'NUMERO_PRODUCTO')]/../../../td[8]";
	By btnModificar = By.xpath("//*[@id='cphCuerpo_gvCuentasInscritas_lnkNumerodeCuenta_0']");
	String modificar = "//*[@id='cphCuerpo_gvCuentasInscritas']/tbody/tr/td[contains(text(),'TITULAR')]/../td[contains(text(),'NUMERO_IDENTIFICACION')]/../td/center/a[contains(text(),'NUMERO_PRODUCTO')]";
	By modificarCorreo = By.id("cphCuerpo_txtDetalleCorreoElectrico");
	By btnAprobarModificacion = By.id("cphCuerpo_btnDetalleModificar");
	By btnVolverModificar = By.id("cphCuerpo_btnvolvermodificar");

// -----------------------------------------------------------------------------------------------------------------------
	// LOCATORS PARA REALIZAR LA NUEVA INSCRIPCIÓN
	By btnNuevaInscripcion = By.xpath("//*[@value='Nueva Inscripción']");

//	By tipoDestino = By.xpath("//tr/td/b[text()='Tipo de destino:']/following::td[3]/select");
	By tipoDestino = By.id("cphCuerpo_ddlInscripcionTipoDestino");
//	By bancoDestino = By.xpath("//tr/td/b[text()='Banco de destino:']/following::td[1]/select");
	By bancoDestino = By.id("cphCuerpo_ddlIncripcionBancoDestino");
	By numeroDestino = By.id("cphCuerpo_txtInscripcionNumeroDestino");
	By nombreTitular = By.id("cphCuerpo_txtInscripcionNombreTitular");
	By tipoIdentificacion = By.id("cphCuerpo_ddlInscripcionTipoIDTitular");
	By numeroIdentificacion = By.id("cphCuerpo_txtInscripcionIDTitular");
	By correo = By.id("cphCuerpo_txtInscripcionCorreoElctronico");

	// Este campo "correoElectronico" es opcional al momento de ingresarlo
	By btnAdicionarInscripcion = By.xpath("//input[@value='Adicionar inscripción']");

// -----------------------------------------------------------------------------------------------------------------------
	// LOCATORS PARA LA INSCRIPCIÓN (Inscripciones desde excel)
	By formatoExcel = By.id("cphCuerpo_dropFormatoins");
	By rutaExcel = By.id("fileName");
	By btnBuscarArchivo = By.xpath("//*[@value='Buscar Archivo']");
	By btnCargarArchivo = By.id("cphCuerpo_btnCargarArchivo");

	// lOCATORS PARA APROBAR INSCRIPCIÓN
	By btnAprobarInscripcion = By.id("cphCuerpo_btnCrearInscripcionCuenta");
	By otp = By.id("cphCuerpo_tbxTokenOtp");
	By btnConfirmarTx = By.id("cphCuerpo_btnConfirmarTransaccion");
	By btnVolverResgistrar = By.id("cphCuerpo_btnvolverregistrar");

	// LOCATOR PARA APROBAR ELIMINACIÓN DE CUENTA
	By btnEliminar = By.id("cphCuerpo_btnElimina");
	By btnAprobarEliminacion = By.id("cphCuerpo_btnEliminarTransaccion");
	By btnVolverEliminar = By.id("cphCuerpo_btnvolvereliminar");
	By tableDeleteAcount = By.xpath("//*[@id='cphCuerpo_gvEliminarCuentas']/tbody");

	// LOCATOR PARA SELECCIONAR REGISTRO
	By tablaInicio = By.id("cphCuerpo_PnProdAhorro");

	int contador = 0;

	public void Inscripcion() throws Exception {

		String typeTest = SettingsRun.getTestData().getParameter("Tipo prueba");
		switch (typeTest) {

		case "Inscribir cuenta":
			this.InscribirCuenta();
			break;

		case "Inscripción por archivo":
			this.InscripcionPorArchivo();
			break;

		case "Modificar cuenta":
			this.ModificarCuenta();
			break;

		case "Eliminar cuenta":
			this.EliminarCuenta();
			break;

		case "Consultar inscripción":
			this.AccesoRuta();
			this.ConsultarCuenta();
			break;

		default:
			break;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------
	// Métodos que permitiran realizar las acciones permitidas en
	// Metodo que permitira realizar una inscripción
	public void InscribirCuenta() throws Exception {
		// Variables las cuales extraen la información del excel
		String destinationType = SettingsRun.getTestData().getParameter("Tipo de destino").trim();
		String bankDestination = SettingsRun.getTestData().getParameter("Banco de destino").trim();
		String numProduct = SettingsRun.getTestData().getParameter("Número de producto destino").trim();
		String ownersName = SettingsRun.getTestData().getParameter("Nombre del titular").trim();
		String identifyType = SettingsRun.getTestData().getParameter("Tipo de identificación").trim();
		String numIdentificacion = SettingsRun.getTestData().getParameter("Número de identificación").trim();
		String email = SettingsRun.getTestData().getParameter("Correo (opc)").trim();

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Información para Inscripción: [ " + destinationType + " - " + bankDestination + " - " + numProduct
						+ " - " + ownersName + " - " + identifyType + " - " + numIdentificacion + " - " + email + " ]");

		this.AccesoRuta();

		this.pageOrigen = new PageOrigen(this);
		do {
			DXCUtil.wait(1);
		} while (this.findElement(btnNuevaInscripcion) == null);
		this.click(btnNuevaInscripcion);

		String[] dataEmpty = { destinationType, bankDestination, numProduct, ownersName, identifyType,
				numIdentificacion };
		for (int i = 0; i < dataEmpty.length; i++) {
			if (!dataEmpty[i].isEmpty()) {
			} else {
				Reporter.reportEvent(Reporter.MIC_WARNING, "No se encuentra la siguiente información: " + dataEmpty
						+ ", por favor verifique la información para realizar la inscripción.");
				this.pageOrigen.terminarIteracion();
			}
		}

		String msg = null;

		msg = this.espera(tipoDestino);
		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incricion");
			this.pageOrigen.terminarIteracion();
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(tipoDestino) == null);

		msg = this.selectListItem(tipoDestino, destinationType);
		Reporter.reportEvent(Reporter.MIC_FAIL, msg);

		msg = this.espera(bancoDestino);
		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incricion");
			this.pageOrigen.terminarIteracion();
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(bancoDestino) == null);

		msg = this.selectListItem(bancoDestino, bankDestination);

		if (msg != null && msg.equals(" ")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.pageOrigen.terminarIteracion();
		}

		msg = this.espera(tipoIdentificacion);

		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incricion");
			this.pageOrigen.terminarIteracion();
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(tipoIdentificacion) == null);
		msg = this.selectListItem(tipoIdentificacion, identifyType);

		if (msg != null && msg.equals(" ")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.pageOrigen.terminarIteracion();
		}

		// Este for permite Ingresar la información de acuerdo a la dataencontrada
		// recoriendo cada campo e ingresando cada información
		if (destinationType.equals("SEGURIDAD SOCIAL Y CESANTÍAS")) {
			// Variable que toma la información del excel volviendolo en lista
			String[] data = { ownersName, numIdentificacion, email };
			// variable donde toma los campos y los vuelve una lista
			By[] fields = { nombreTitular, numeroIdentificacion, correo };
			DXCUtil.wait(1);
			this.ingresarDatos(getDriver(), fields, data);
		} else {

			// Variable que toma la información del excel volviendolo en lista
			String[] data = { numProduct, ownersName, numIdentificacion, email };
			// variable donde toma los campos y los vuelve una lista
			By[] fields = { numeroDestino, nombreTitular, numeroIdentificacion, correo };
			DXCUtil.wait(1);
			this.ingresarDatos(getDriver(), fields, data);
		}
		Reporter.reportEvent(Reporter.MIC_INFO, "Se ingresó correctamente la información en el formulario.");
//		Evidence.save("Inscripción cuenta", this);
		Evidence.save("Inscripción cuenta");
		msg = this.espera(btnAdicionarInscripcion);

		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incripción");
			this.pageOrigen.terminarIteracion();
		}

		msg = this.espera(btnAdicionarInscripcion);

		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incripción.");
			this.pageOrigen.terminarIteracion();
		}

		this.click(btnAdicionarInscripcion);
//		Evidence.save("Inscripciones evidencia", this);
		Evidence.save("Inscripciones evidencia");
		DXCUtil.wait(2);
		this.pagePortalPymes = new PagePortalPymes(this);
		msg = this.pagePortalPymes.getMsgAlertIfExist("lblMasterAlerta");
		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Se visualizo el siguiente mensaje: " + msg);
//			Evidence.save("Inscripciones evidencia", this);
			Evidence.save("Inscripciones evidencia");
			this.pageOrigen.terminarIteracion();
		} else {
//			Evidence.save("Inscripción cuenta", this);
			Evidence.save("Inscripción cuenta");
			this.AprobarTx();
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realizó la inscripción correctamente.");
			this.pageOrigen.terminarIteracion();
		}
	}

	// Método para realizar inscripciones por medio de archivo
	public void InscripcionPorArchivo() throws Exception {
		String formatoArchivo = SettingsRun.getTestData().getParameter("Formato Archivo").trim();
		String rutaArchivo = SettingsRun.getTestData().getParameter("Ruta Archivo").trim();
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		this.pageOrigen = new PageOrigen(this);
		this.AccesoRuta();
		DXCUtil.wait(2);

		String msg = this.espera(btnNuevaInscripcion);
		if (msg != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra boton Incricion");
			this.pageOrigen.terminarIteracion();
		}
		this.click(btnNuevaInscripcion);

		if (tipoPrueba.equals("Inscripción por archivo") && !rutaArchivo.isEmpty()) {
			do {
				DXCUtil.wait(1);
			} while (this.element(formatoExcel) == null);

			msg = this.seleOptionIgual(formatoExcel, formatoArchivo);

			if (msg != null && msg.equals(" ")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.pageOrigen.terminarIteracion();
			}
//			Evidence.save("Inscripcion por archivo", this);
			Evidence.save("Inscripcion por archivo");
			this.click(btnBuscarArchivo);

			this.uploadFile(rutaArchivo);
			DXCUtil.wait(2);
			this.findElement(btnCargarArchivo).click();
			DXCUtil.wait(30);
//			Evidence.save("Inscripcion por archivo", this);
			Evidence.save("Inscripcion por archivo");
			this.click(btnAprobarInscripcion);
			DXCUtil.wait(1);
			this.AprobarTx();
//			Evidence.save("Inscripcion por archivo", this);
			Evidence.save("Inscripcion por archivo");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realizaron las inscripciones correctamente.");
			this.pageOrigen.terminarIteracion();

		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No se ha encontrado una ruta esfecifica para realizar el cargue, por favor verifique el documento.");
		}
	}

	// Metódo que permite modificar alguna cuenta inscrita
	public void ModificarCuenta() throws Exception {

		this.pageOrigen = new PageOrigen(this);
		String numProduct = SettingsRun.getTestData().getParameter("Número de producto destino").trim();
		String ownersName = SettingsRun.getTestData().getParameter("Nombre del titular").trim();
		String identificationNumber = SettingsRun.getTestData().getParameter("Número de identificación").trim();
		String email = SettingsRun.getTestData().getParameter("Correo (opc)").trim();

		this.AccesoRuta();

		this.ConsultarCuenta();
		DXCUtil.wait(1);
//		Evidence.save("Evidencia modificar cuenta", this);
		Evidence.save("Evidencia modificar cuenta");
		String numeroProd = numProduct.replace(numProduct, DXCUtil.right(numProduct, 4));

		String textNumProduct = this.findElement(btnModificar).getText();
		String txtNumProduct = textNumProduct.replace(textNumProduct, DXCUtil.right(textNumProduct, 4));

		String btnModify = modificar.replace("NUMERO_PRODUCTO", numeroProd)
				.replace("NUMERO_IDENTIFICACION", identificationNumber).replace("TITULAR", ownersName);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se valida la siguiente información: Texto encontrado en Pyme: "
				+ txtNumProduct + ", Texto ingresago en el Excel: " + numeroProd);

		if (!ownersName.isEmpty() && !identificationNumber.isEmpty() && !numProduct.isEmpty()
				&& numeroProd == txtNumProduct) {
			this.findElement(By.xpath(btnModify));
//			Evidence.save("Evidencia modificar cuenta", this);
			Evidence.save("Evidencia modificar cuenta");
		} else {
			this.click(btnModificar);
//			Evidence.save("Evidencia modificar cuenta", this);
			Evidence.save("Evidencia modificar cuenta");
		}

		WebElement editEmail = this.findElement(modificarCorreo);
		this.clearInputbox(editEmail);
		editEmail.sendKeys(email);
//		Evidence.save("Evidencia modificar cuenta", this);
		Evidence.save("Evidencia modificar cuenta");
		DXCUtil.wait(1);
		this.AprobarTx();
//		Evidence.save("Evidencia modificar cuenta", this);
		Evidence.save("Evidencia modificar cuenta");
		Reporter.reportEvent(Reporter.MIC_PASS, "Se realizó la Modificación correctamente.");
		this.pageOrigen.terminarIteracion();
	}

	// Metódo que permite eliminar alguna cuenta inscrita
	public void EliminarCuenta() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		this.AccesoRuta();

		WebElement seleImput = null;

		this.ConsultarCuenta();
		String destinationType = SettingsRun.getTestData().getParameter("Tipo de destino").trim();

		String productNumber = SettingsRun.getTestData().getParameter("Número de producto destino").trim();
		String ownersName = SettingsRun.getTestData().getParameter("Nombre del titular").trim();
		String identificationNumber = SettingsRun.getTestData().getParameter("Número de identificación").trim();

		String numeroProd = productNumber.replace(productNumber, DXCUtil.right(productNumber, 4));

		String selectResultSegSocial = selectionOption.replace("TITULAR", ownersName)
				.replace("NUMERO_IDENTIFICACION", identificationNumber).replace("NUMERO_PRODUCTO", "0");
		String selectResult = selectionOption.replace("TITULAR", ownersName)
				.replace("NUMERO_IDENTIFICACION", identificationNumber).replace("NUMERO_PRODUCTO", numeroProd);
		String selectResultEstado = selectionOptionEstado.replace("TITULAR", ownersName)
				.replace("NUMERO_IDENTIFICACION", identificationNumber).replace("NUMERO_PRODUCTO", numeroProd);

		DXCUtil.wait(2);

		// Este for permite Ingresar la información de acuerdo a la dataencontrada
		// recoriendo cada campo e ingresando cada información
		if (destinationType.equals("SEGURIDAD SOCIAL Y CESANTÍAS")) {
			if (this.element(By.xpath(selectResultSegSocial)) != null) {
				seleImput = this.element(By.xpath(selectResultSegSocial));
				this.element(By.xpath(selectResultSegSocial)).click();
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro los datos a e liminar: " + productNumber
						+ " " + ownersName + identificationNumber);
			}
		} else {
			if (this.element(By.xpath(selectResult)) != null) {
				seleImput = this.element(By.xpath(selectResult));
				this.element(By.xpath(selectResult)).click();
			} else {
			if (this.element(By.xpath(selectResultEstado)) != null) {
				String msge = this.getText(By.xpath(selectResultEstado));
				Reporter.reportEvent(Reporter.MIC_WARNING, "Estado de la Inscripción: " + msge);
			}else if (this.element(By.xpath(selectResultEstado)) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro los datos a e liminar: " + productNumber+ " " + ownersName + " " + identificationNumber);
				}

			}
		}
		DXCUtil.wait(1);
//		Evidence.save("Evidencias eliminar cuenta", this);
		Evidence.save("Evidencias eliminar cuenta");
		if (this.element(btnEliminar) != null && seleImput != null) {
			this.click(btnEliminar);
			DXCUtil.wait(2);
//		Evidence.save("Evidencias eliminar cuenta", this);
			Evidence.save("Evidencias eliminar cuenta");

			if (this.element(btnAprobarEliminacion) != null)
				this.element(btnAprobarEliminacion).click();

			this.existDialogAccept();
//		Evidence.save("Evidencias eliminar cuenta", this);
			Evidence.save("Evidencias eliminar cuenta");
			this.AprobarTx();
//		Evidence.save("Evidencias eliminar cuenta", this);
			Evidence.save("Evidencias eliminar cuenta");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se realizó la eliminación de la cuenta inscrita correctamente.");

			this.pageOrigen.terminarIteracion();
		} else {

			Reporter.reportEvent(Reporter.MIC_PASS, "No se realizó la eliminación de la cuenta inscrita");
			this.pageOrigen.terminarIteracion();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
//-- Métododos para realizar validaciones en el portal-------------------------------------------------------------------------------------------------------------------------------------
	// Método para acceder a la ruta haciendo algunas validaciones
	public void AccesoRuta() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		String msgError = null;

		WebElement aux = this.element(tablaInicio);
		int cont = 0;
		do {
			cont++;
			DXCUtil.wait(1);
			if (cont > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut Busqueda empresa");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(tablaInicio) == aux);

		String pagoyTx = this.pageOrigen.getTextMid();
		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		if (navegador.contains("CHROME")) {
			if (pagoyTx.contains("Pagos, Transferencias y otros")) {
				DXCUtil.wait(1);
				msgError = pageOrigen.irAOpcion("", "Pagos, Transferencias y otros", "Inscripciones");
			} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {
				DXCUtil.wait(1);
				msgError = pageOrigen.irAOpcion("", "Pagos, Transferencias y Otros", "Inscripciones");

			} else {
				msgError = pageOrigen.irAOpcion("", "Pagos y Transferencias", "Inscripciones");
			}
			if (msgError != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, msgError);
				this.pageOrigen.terminarIteracion();
			}
		} else {
			msgError = pageOrigen.irAOpcionMoZilla("", "Pagos, Transferencias y Otros", null, null, null);

			if (pagoyTx.contains("Pagos, Transferencias y otros")) {
				DXCUtil.wait(1);
				msgError = pageOrigen.irAOpcionMoZilla("", "Pagos, Transferencias y otros", "Inscripciones", null,
						null);
			} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {
				DXCUtil.wait(1);
				msgError = pageOrigen.irAOpcionMoZilla("", "Pagos, Transferencias y Otros", "Inscripciones", null,
						null);

			} else if (pagoyTx.contains("Pagos y Transferencias")) {
				DXCUtil.wait(1);
				msgError = pageOrigen.irAOpcionMoZilla("", "Pagos y Transferencias", "Inscripciones", null, null);
			}
			if (msgError != null) {
				Reporter.reportEvent(Reporter.MIC_WARNING, msgError);
				this.pageOrigen.terminarIteracion();
			}
		}
//		Evidence.save("Inscripciones", this);
		Evidence.save("Inscripciones");
	}

	// Método que permite consultar una cuenta inscrita para poder realizarle una
	// acción como: edición o eliminación.
	public void ConsultarCuenta() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		String destinationType = SettingsRun.getTestData().getParameter("Tipo de destino").trim();

		String bank = SettingsRun.getTestData().getParameter("Banco para consultar").trim();
		String productNumber = SettingsRun.getTestData().getParameter("Número de producto destino").trim();
		String ownersName = SettingsRun.getTestData().getParameter("Nombre del titular").trim();
		String identificationNumber = SettingsRun.getTestData().getParameter("Número de identificación").trim();
//		String identificationNumber = SettingsRun.getTestData().getParameter("Número de identificación").trim();

		this.espera(selectBanco);

		String msg = this.seleOptionIgual(selectBanco, bank);

		if (msg != null && msg.equals(" ")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.pageOrigen.terminarIteracion();
		}
		Reporter.reportEvent(Reporter.MIC_INFO, "La información a consultar es la siguiente: " + bank + " "
				+ productNumber + " " + ownersName + " " + identificationNumber);

		// Este for permite Ingresar la información de acuerdo a la dataencontrada
		// recoriendo cada campo e ingresando cada información
		if (destinationType.equals("SEGURIDAD SOCIAL Y CESANTÍAS")) {
			// Variable que toma la información del excel volviendolo en lista
			String data[] = { "0", ownersName, identificationNumber };
			// variable donde toma los campos y los vuelve una lista
			By fields[] = { numeroProductDestino, nombTitular, identificTitular };
			DXCUtil.wait(1);
			this.ingresarDatos(getDriver(), fields, data);
		} else {

			// Variable que toma la información del excel volviendolo en lista
			String data[] = { productNumber, ownersName, identificationNumber };
			// variable donde toma los campos y los vuelve una lista
			By fields[] = { numeroProductDestino, nombTitular, identificTitular };
			DXCUtil.wait(1);
			this.ingresarDatos(getDriver(), fields, data);
		}

		if (bank != "Todos" && productNumber.isEmpty() && ownersName.isEmpty() && identificationNumber.isEmpty()) {
			int YES_NO_OPTION = 0;
			JOptionPane.showInternalConfirmDialog(null,
					"Se encontró información unicamente en la columna (Banco para Consultar), \n se realizara la acción al primer resultado o cancele e ingrese información más detallada \n para hacer la transacción a la cuenta inscrita correcta. \n Cancele (Si) o continue(No) la operación.",
					"Cancele (Si) o continue(No) la operación.", YES_NO_OPTION);
		}

		this.click(btnBuscar);
		DXCUtil.wait(2);

		this.pagePortalPymes = new PagePortalPymes(this);
		String mensaje = this.pagePortalPymes.getMsgAlertIfExist("lblMasterAlerta");

		if (mensaje != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Se visualizo el siguiente mensaje informativo: " + mensaje);
			this.pageOrigen.terminarIteracion();
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se realizo la inscripción correctamente.");
		}

	}

	// Método que aprobara las TX y se pueda guardar la acción realizada,
	// permitiendo la OTP, token, etc.
	public void AprobarTx() throws Exception {
		this.pageOrigen = new PageOrigen(this);

		this.pageConfirmacion = new PageConfirmacion(this);
		this.pageConfirmacion.aprobarTx();

		this.pagePortalPymes = new PagePortalPymes(this);
		String mensaje = this.pagePortalPymes.getMsgAlertIfExist("lblMasterAlerta");

		if (mensaje != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Se visualizo el siguiente mensaje informativo: " + mensaje);
			this.pageOrigen.terminarIteracion();
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se realizo la inscripción correctamente.");
		}
	}

	public String espera(By locator) throws Exception {
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador > 30) {
				return "No se encontro el elemnto";
			}
		} while (this.element(locator) == null);

		return null;
	}

	// Permite ingresar los datos en cada campo
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
						"No se encontraron los campos: " + field + ", por favor verifique la información.");
				SettingsRun.exitTestIteration();
			}
		}

		Evidence.save("consulta");
	}

	public static void uploadFile(String fileLocation) throws Exception {
		// Copia la ubicación del archivo al portapapeles
		StringSelection stringSelection = new StringSelection(fileLocation);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		// Simula las teclas Ctrl+V para pegar la ubicación del archivo en el diálogo
		// del explorador de archivos
		Robot robot = new Robot();
		DXCUtil.wait(1);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Evidence.save("Inscripcion por archivo");
		DXCUtil.wait(1);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

}
