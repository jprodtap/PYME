package dav.middlePymes;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
//import dxc.execution.Evidence;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageAdministracion extends PageInicioMiddle {

	public static final String MSG_NO_HAY_REGISTROS = "NO SE ENCONTRARON DATOS";
	public static final String MSG_CREAR_EMPRESA = "Empresa grabada exitosamente.";
	public static final String MSG_MODI_EMPRESA = "Se modificó la empresa exitosamente";
	public static final String MSG_EMPRESA_ASOC = "LA EMPRESA YA ESTÁ ASOCIADA";

//=======================================================================================================================
	// LOCATORS Y EXPRESIONES PARA ENCONTRAR LOS OBJETOS EN MIDDLE PYME
	String xPathClienEmp = "//input[@id='cphCuerpo_txtNumeroCliente' or @id='cphCuerpo_txtBQNumeroClienteEmpresarial' or @id='cphCuerpo_txtDEClienteEmpresarial']";// input
	By inputcliemp = By.id("cphCuerpo_txtDEClienteEmpresarial");
	By locBusClieEmp = By.id("cphCuerpo_btnBuscar"); // INPUT
	By locMensajeAlert = By.id("lblMasterAlerta"); // SPAN
	String xPathEstado = "//span[contains(text(),'(CE)')][1]//parent::td//following-sibling::td[4]";
	By locEditar = By.id("cphCuerpo_gvClienteEmpresarial_lnkNumeroCliente1_0");// a
	By locTopeInf = By.id("cphCuerpo_txtCMTopeInferiorSaldoPromedio");// input cphCuerpo_txtCMTopeInferiorSaldoPromedio
	By locTopeSup = By.id("cphCuerpo_txtCMTopeSuperiorSaldoPromedio");// input
	By locSaldProme = By.id("cphCuerpo_lblCMSaldoPromedioMesAnterior");// span
	By locIndiCum = By.id("cphCuerpo_ddlCMIndicadorCumplimientoSaldoPromedio");// span
	By locVigenc = By.id("cphCuerpo_ddlCMVigencia");// select
	By locActualiz = By.id("cphCuerpo_btnCMActualizar");// input
	By locNumIdEmp = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By locNumIdEmp2 = By.id("cphCuerpo_txtNumeroIdentificacion");
	String xPathEditar = "//td[contains(text(),'(CE)')][1]//following-sibling::td[contains(text(),'(NUM_ID)')][1]//following-sibling::td//a[contains(text(),'Editar')]";
	By locTipId = By.id("cphCuerpo_ddlDETipoIdentificacion");
	By locNumEmp = By.id("cphCuerpo_txtDENumeroIdentificacion");
	By locCombo = By.id("cphCuerpo_ddlDEComboServicios");
	By locEjecCue = By.id("cphCuerpo_txtDEEjecutivoCuenta"); // cphCuerpo_txtDEEjecutivoCuenta
	By locEmaConOpe = By.id("cphCuerpo_txtDEEmailContactoOperativo");// cphCuerpo_txtDEEmailContactoOperativo
	By locEmaPagAut = By.id("cphCuerpo_txtDEEmailPagosAutomaticos");
	By locEmaContComer = By.id("cphCuerpo_txtDEEmailContactoComercial");
	By locRbInscSi = By.id("cphCuerpo_rdbDEValidaInscripcionesSi");
	By locRbInscNo = By.id("cphCuerpo_rdbDEValidaInscripcionesNo");
	By locFirmas = By.id("cphCuerpo_ddlDEAprobacionesEjecutarTrx");
	By locTipProd = By.id("cphCuerpo_ddlDETipoProducto");
	By locNumProd = By.id("cphCuerpo_ddlDENumeroProducto");
	By locTipIdUsu = By.id("cphCuerpo_ddlDEATipoIdentificacion");
	By locNumId = By.id("cphCuerpo_txtDEANumeroIdentificacion");
	By locNomAdm = By.id("cphCuerpo_txtDEANombreAdministrador");
	By locApellAdm = By.id("cphCuerpo_txtDEAApellidosAdministrador");
	By locEmail = By.id("cphCuerpo_txtDEAEmail");
	By locTelef = By.id("cphCuerpo_txtDEATelefono");
	By locMovil = By.id("cphCuerpo_txtDEAMovil");
	By locDirec = By.id("cphCuerpo_txtDEADireccion");
	By locCiudad = By.id("cphCuerpo_dropCiudad");
	By locRbAsocClie = By.id("cphCuerpo_rdbDEAAsociarClienteEmpresarial");
	String xPathEmp = "//td[(text()='(CE)')][1]//following-sibling::td[(text()='(NUM_ID)')][1]//following-sibling::td[1]";
	String xPathBusque = "//td[contains(text(),'(NUM_ID)')][1]//preceding-sibling::td[2]";

	public PageAdministracion(PageLoginPymes parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite consultar el cliente empresarial con el fin de saber si
	 * existe, sino existe retorna de mensaje de alerta,<br>
	 * si existe el cliente empresarial consultado realiza la consulta y realiza la
	 * captura de los siguientes datos. arrRetorno[0]= Mensaje de alerta o NULL si
	 * todo fue correcto arrRetorno[1]= Saldo Tope Inferior arrRetorno[2]= Saldo
	 * Tope Superior arrRetorno[3]= Saldo Promedio. Retorna el array [arrRetorno]
	 * con la informacion del cliente Empresarial.
	 */

	public String[] validarClienteEmp(String clienEmp, boolean indic) throws Exception {
		String[] arrRetorno = new String[4];
		DXCUtil.wait(3);
		this.irAOpcion("Administración de Cliente Empresarial", "Administración", "Cliente Empresarial");
		WebElement objClienteEmp;
		do {
			DXCUtil.wait(1);
//			DXCUtil.Movercursor();
			objClienteEmp = this.element(xPathClienEmp);
		} while (!objClienteEmp.isDisplayed()); // ESPERA A QUE SE PRESENTE EL CAMPO DE CLIENTE EMPRESARIAL
		this.write(objClienteEmp, clienEmp);
		this.clickButton("Buscar Cliente Empresarial");
		String path = xPathEstado.replace("(CE)", clienEmp);
		WebElement objResul;
		String msgAlerta = null;
		// SE ESPERA QUE SE PRESNTE LA GRILLA DE RESULTADOS O RETORNE EL MENSAJE SINO
		// ENCUENTRA DATOS.
		do {
			DXCUtil.wait(1);
//			 DXCUtil.Movercursor();
			objResul = this.element(path);
			if (objResul == null) {
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			}
		} while (objResul == null && msgAlerta == null);
//		Evidence.save("consultaClienteEmpresarial", this);
		Evidence.save("consultaClienteEmpresarial");
		if (msgAlerta != null)
			arrRetorno[0] = msgAlerta.toUpperCase();
		if (msgAlerta != null) {
			return arrRetorno;
		} else if (objResul != null) {
			arrRetorno[0] = null;
			List<WebElement> campEditar = this.findElements(locEditar);
			this.click(campEditar.get(0));
			DXCUtil.wait(1);
			WebElement objSaldoInf;
			do {
				DXCUtil.wait(1);
//				 DXCUtil.Movercursor();
				objSaldoInf = this.element(locTopeInf);
			} while (!this.isDisplayed(objSaldoInf));
			// ESPERA A QUE SE PRESENTE EL CAMPO SALDO INFERIOR, SE CAPTURAN LOS DATOS DE
			// CADA UNO DE LOS CAMPOS
			arrRetorno[1] = objSaldoInf.getAttribute("value");
			arrRetorno[2] = this.element(locTopeSup).getAttribute("value");
			arrRetorno[3] = this.element(locSaldProme).getText();
			if (!indic)
				this.selectListItem(locIndiCum, "NO");
			else
				this.selectListItem(locIndiCum, "SI");
			this.clickButton("Actualizar");
			do {
				DXCUtil.wait(1);
//				 DXCUtil.Movercursor();
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			} while (msgAlerta == null);
			Evidence.saveFullPage("detalle[" + clienEmp + "]", this);
			this.clickButton("Regresar");
			do {
				DXCUtil.wait(1);
//				 DXCUtil.Movercursor();
				objResul = this.element(path);

			} while (objResul == null);
			DXCUtil.wait(3);
			this.moveUpScreen();
		}

		return arrRetorno;
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite consultar la asociación del cliente empresarial con la
	 * empresa, si existe realiza la parametrización <br>
	 * pertinente, sino existe la empresa la contrata o si no retorna el mensaje de
	 * error DLC retorna NULL.
	 */
	public String[] asocEmpreClienEmp(String clienEmp, String tipId, String idEmpresa, String numIdUser,
			String tipoIDUser, String combo, String numFirmas, boolean incrip) throws Exception {
		String[] arrayReturn = new String[2];

		this.moveUpScreen();
		String msg = this.irAOpcion("Administración de Empresas", "Administración", "Empresa");
		if (msg != null) {
			String[] msg1 = { msg };
			return arrayReturn = msg1;
		}

		WebElement objClienteEmp;
		do {
			DXCUtil.wait(1);
			objClienteEmp = this.element(xPathClienEmp);
		} while (!objClienteEmp.isDisplayed()); // ESPERA A QUE SE PRESENTE EL CAMPO
		this.write(objClienteEmp, clienEmp);
		DXCUtil.wait(1);
		if (this.element(locNumIdEmp) != null) {
			this.write(locNumIdEmp, idEmpresa);

		}
		if (this.element(locNumIdEmp2) != null) {
			this.write(locNumIdEmp2, idEmpresa);

		}
		DXCUtil.wait(1);
		this.clickButton("Buscar Empresa");

// SE REEMPLAZA EN EL XPATH EL NUMERO DE CLIENTE EDXCUtil.wait(2);MPRESARIAL Y EL NUMERO DE ID DE LA EMPRESA.
		String path = xPathEditar.replace("(CE)", clienEmp).replace("(NUM_ID)", idEmpresa);
		List<WebElement> objResul;
		WebElement objMensaje = null;
		arrayReturn[0] = null;
		do {
			DXCUtil.wait(1);
			objResul = this.findElements(By.xpath(path));
			if (objResul.isEmpty()) {
				objMensaje = this.element(locMensajeAlert);
			}
		} while (objResul.isEmpty() && objMensaje == null);
		Evidence.saveFullPage("consultaEmpresa[" + idEmpresa + "]", this);
		if (!objResul.isEmpty()) {
			this.click(objResul.get(0));
// SE LLAMA METODO DE EDITAR EMPRESA
			String msgError = this.editarEmpresa(combo, numFirmas, incrip);
			if (msgError != null) {
				arrayReturn[0] = msgError;
			}
		} else if (objMensaje != null) {
// SE LLAMA METODO DE CREAR EMPRESA
			String msgError = this.crearEmpresa(clienEmp, tipId, idEmpresa, combo, numFirmas, tipoIDUser, numIdUser,
					incrip);
			if (msgError != null) {
				arrayReturn[0] = msgError;
			}
		}
		List<WebElement> objResulnom;
		String nomEmp = xPathEmp.replace("(CE)", clienEmp).replace("(NUM_ID)", idEmpresa);

		if (arrayReturn[0] == null || (arrayReturn[0] != null && arrayReturn[0].isEmpty())) {
			do {
				DXCUtil.wait(1);
				objResulnom = this.findElements(By.xpath(nomEmp));
			} while (objResulnom == null);
			if (!objResulnom.isEmpty()) {
				arrayReturn[0] = getText(objResulnom.get(0));
			}
		}
		return arrayReturn;
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite realizar la creación de la empresa y asociarla al cliente
	 * empresarial sino estaba asociada. Adicionalmente toma los datos de la prueba
	 * conmo los son el combo,número de firmas, indicador de inscripciones,<br>
	 * tipo de id del usuario, número de id del usuario y los parametriza. Retorna
	 * NULL si fue posible la creación de la empresa. Retorna [msgAlerta] con el
	 * mensaje exitoso o con el mensaje del error.
	 */
	public String crearEmpresa(String clienteEmp, String tipoId, String numDoc, String combo, String firmas,
			String TipIdUser, String numIdUser, boolean isInscrip) throws Exception {
		this.clickButton("Nueva Empresa");

		String Nombre = SettingsRun.getTestData().getParameter("Nombre de Usuario").trim();
		String Apellido = SettingsRun.getTestData().getParameter("Apellido_Usuario").trim();

		String email = "Pruebas@dav.com";
		String telef = "5689741";
		String celular = "3156638290";
		String direc = "cra 25 25 85";
		String ciudad = "BOGOTA D.C.";
		String msgError;
		String msgAlcomb = "";
		WebElement objTipId;
		do {
			DXCUtil.wait(1);
//			DXCUtil.Movercursor();
			objTipId = this.element(locTipId);

		} while (objTipId == null); // ESPERA A QUE SE PRESENTE EL CAMPO TIPO DE IDENTIFICIÓN
		this.selectListItem(objTipId, tipoId);
		this.write(locNumEmp, numDoc);
//		Evidence.save("verificacionEmpresa", this);
		Evidence.save("verificacionEmpresa");
		this.clickButton("Verificar Empresa");
		WebElement objcombo = null;
		String msgAlerta = null;
		// SELECCIONA EL COMBO QUE SE ENVIE POR PARAMETRO O SINO DEJA LA OPCION POR
		// DEFAULT "SIN COMBOS".
		do {
			DXCUtil.wait(1);
//			DXCUtil.Movercursor();
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			if (msgAlerta == null) {
				objcombo = this.element(locCombo);
			}
//			DXCUtil.Movercursor();
		} while (!objcombo.isDisplayed() && msgAlerta == null);

		if (msgAlerta != null) {
//			Evidence.save(msgAlerta, this);
			Evidence.save(msgAlerta);
			return msgAlerta;
		} else {
			// SE PROCEDE A RELIZAR EL LLENADO DE LOS DATOS DE LA EMPRESA CON ALGUNOS DATOS
			// MAPEADOS QUE NO SON IMPORTANTES.

			if (!combo.isEmpty()) {
				msgAlcomb = this.seleOption(locCombo, combo);
				if (msgAlcomb == null) {
					System.out.println("No se encontro el combo: [" + msgAlcomb + "], se selecciona SIN COMBOS");
					this.selectListItem(locCombo, "SIN COMBOS");
				}
			}
			this.write(locEjecCue, Nombre);
			this.write(locEmaConOpe, email);
			this.write(locEmaPagAut, email);
			this.write(locEmaContComer, email);
			// SELECCIONA EL INDICADOR DE INSCRIPCIONES QUE SE ENVIE POR PARAMETRO O SINO
			// DEJA LA OPCION POR DEFAULT "NO".
			if (isInscrip)
				this.click(locRbInscSi);
			else
				this.click(locRbInscNo);
			// SELECCIONA EL NUMERO DE FIRMAS ENVIADO POR PARAMETRO O SINO DEJA LA OPCION
			// POR DEFAULT "1".
			msgAlcomb = this.seleOption(locFirmas, firmas);
			if (msgAlcomb == null) {
				System.out.println("No se encontro las firmas: [" + msgAlcomb + "], se selecciona 1 por defecto");
				this.selectListItem(locFirmas, "1");
			}
			// this.selectListItem(locTipProd, tipCta.toUpperCase());
			this.selectListItemRandom(locTipProd, false);// SE SELECCIONA EL TIPO DE PRODUCTO ALEATORIAMENTE.

			do {
				DXCUtil.wait(1);
			} while (!this.isDisplayed(locNumProd));

			// SE ESPERA QUE CARGUE LA LISTA CON LAS CUENTAS.
			WebElement objnumCta = this.element(locNumProd);
			String itemInicial = objnumCta.getText();
			String itemFinal;
			do {
				DXCUtil.wait(1);
//					DXCUtil.Movercursor();
				objnumCta = this.element(locNumProd);
				itemFinal = objnumCta.getText();
			} while (itemInicial == itemFinal);
			// this.selectListItem(locNumProd, nroCta);
//			Evidence.save("Usuario", this);
			Evidence.save("Usuario");
			DXCUtil.wait(2);
			this.selectListItemRandom(locNumProd, true);// SE SELECCIONA EL NÚMERO DE PRODUCTO ALEATORIAMENTE.
			DXCUtil.wait(2);
			// EDITA LOS DATOS DEL USUARIO QUE SE RECIBEN POR PARAMETRO QUE DEBEN SER
			// CAPTURADOS DE LA GLOBAL DATA.
			this.selectListItem(locTipIdUsu, TipIdUser);
			this.write(locNumId, numIdUser);
			this.write(locNomAdm, Nombre);
			this.write(locApellAdm, Apellido);
			this.write(locEmail, email);
			this.write(locTelef, telef);
			this.write(locMovil, celular);
			this.write(locDirec, direc);
			this.selectListItem(locCiudad, ciudad);
			this.click(locRbAsocClie);
			WebElement objClienteEmp;
			objClienteEmp = this.element(xPathClienEmp);
			do {
				DXCUtil.wait(2);
			} while (!this.isEnabled(inputcliemp));

			this.write(inputcliemp, clienteEmp);

			DXCUtil.wait(1);
			this.clickButton("Buscar Cliente Empresarial");
			DXCUtil.wait(3);
			this.clickButton("Crear");
			DXCUtil.wait(3);
			Evidence.saveFullPage("creacionEmpresa[" + numDoc + "]", this);
			// ESTA FUNCION PERMITE VALIDAR SI SE MUESTRA EL MENSAJE DE CREACIÓN DE EMPRESA
			// EXITOSA O SI SE PRESENTA ALGUN ERROR AL CREAR LA EMPRESA.
			do {
//					DXCUtil.Movercursor();
				DXCUtil.wait(1);
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			} while (msgAlerta == null);

			DXCUtil.wait(3);

			if (!msgAlerta.contains(MSG_CREAR_EMPRESA) || !msgAlerta.contains("exitosamente")) {
				Evidence.saveFullPage(msgAlerta, this);
				DXCUtil.wait(1);
				this.clickButton("Regresar");
				boolean objDialog;

				do {
					DXCUtil.wait(1);
					objDialog = this.existDialog();
//						DXCUtil.Movercursor();
				} while (!objDialog);

				this.acceptDialog();

				return msgAlerta;

			}
		}
		return null; // SI LLEGA A ESTE PUNTO ES PORQUE NO HUBO ERRORES
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite realizar la edición de la empresa donde recibe por
	 * parametro el combo, el numero de firmas y si se necesita<br>
	 * que valide inscripciones, sino el los asigna por Default combo [SIN COMBOS],
	 * número de firmas[1], valida inscripciones [NO]. Retorna NULL si fue posible
	 * la creación de la empresa. Retorna [msgAlerta] con el mensaje.
	 */
	public String editarEmpresa(String combo, String numfirmas, boolean isIncrip) throws Exception {
		String msgError;
		String msgAlcomb = "";
		// SELECCIONA EL COMBO QUE SE ENVIE POR PARAMETRO O SINO DEJA LA OPCION POR
		// DEFAULT "SIN COMBOS".
		if (!combo.isEmpty()) {
			msgAlcomb = this.seleOption(locCombo, combo);
			if (msgAlcomb == null) {
				System.out.println("No se encontro el combo: [" + msgAlcomb + "], se selecciona SIN COMBOS");
				this.selectListItem(locCombo, "SIN COMBOS");
			}
		}
		// SELECCIONA EL INDICADOR DE INSCRIPCIONES QUE SE ENVIE POR PARAMETRO O SINO
		// DEJA LA OPCION POR DEFAULT "NO".
		if (isIncrip)
			this.click(locRbInscSi);
		else
			this.click(locRbInscNo);
		// SELECCIONA EL NUMERO DE FIRMAS ENVIADO POR PARAMETRO O SINO DEJA LA OPCION
		// POR DEFAULT "1".
		if (numfirmas.isEmpty()) {
			numfirmas = "1";
	}
		msgAlcomb = this.seleOption(locFirmas, numfirmas);
		if (!msgAlcomb.isEmpty()) {
//			System.out.println("No se encontro las firmas: ["+ msgAlcomb +"], se selecciona 1 por defecto");
			this.selectListItem(locFirmas, numfirmas);
		}
		this.clickButton("Actualizar");
		Evidence.saveFullPage("edicionEmpresa", this);
		String msgAlerta = null;
		// ESTA FUNCION PERMITE VALIDAR SI SE MUESTRA EL MENSAJE DE EDICIÓN EXITOSA O SI
		// SE PRESENTA ALGUN ERROR AL EDITAR LA EMPRESA.
		do {
			DXCUtil.wait(1);
//			DXCUtil.Movercursor();
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (msgAlerta == null);
		if (!msgAlerta.toUpperCase().contains(MSG_MODI_EMPRESA)) {
//			Evidence.save(msgAlerta);
			return msgAlerta;
		}
		this.clickButton("Regresar");
		boolean objDialog;
		do {
			DXCUtil.wait(1);
//			DXCUtil.Movercursor();
			objDialog = this.existDialog();
		} while (!objDialog);
		this.acceptDialog();
		return null; // SI LLEGA A ESTE PUNTO ES PORQUE NO HUBO ERRORES
	}

	// ***********************************************************************************************************************
	/**
	 * Este metodo permite consultar la empresa, si tiene algun cliente empresarial
	 * asociado.
	 */
	public String valEmpresa(String idEmpresa) throws Exception {
		WebElement objClienteEmp;
		this.irAOpcion("Administración de Empresas", "Administración", "Empresa");
		do {
			DXCUtil.wait(1);
//				DXCUtil.Movercursor();
			objClienteEmp = this.element(xPathClienEmp);
		} while (!objClienteEmp.isDisplayed()); // ESPERA A QUE SE PRESENTE EL CAMPO
		this.write(locNumIdEmp, idEmpresa);
		this.clickButton("Buscar Empresa");
//		Evidence.save("consultaEmpresa", this);
		Evidence.save("consultaEmpresa");
		// SE REEMPLAZA EN EL XPATH EL NUMERO DE CLIENTE EMPRESARIAL Y EL NUMERO DE ID
		// DE LA EMPRESA.
		String path = xPathBusque.replace("(NUM_ID)", idEmpresa);

		WebElement objMensaje = null;
		String cliEmp = null;
		String msgError = null;
		WebElement objResul = null;
		// SI ENCUENTRA VARIOS ELEMENTOS EN LA GRILLA TOMARA EL ULTIMO
		if (this.findElements(By.xpath(path)).size() != 0) {
			objResul = this.findElements(By.xpath(path)).get(this.findElements(By.xpath(path)).size() - 1);
		}
		do {
			DXCUtil.wait(1);
//				DXCUtil.Movercursor();
			objMensaje = this.element(locMensajeAlert);
			if (objMensaje != null)
				msgError = objMensaje.getText().toUpperCase();

		} while (objResul == null && objMensaje == null);

		if (objResul != null) {
			cliEmp = objResul.getText();
			return cliEmp;
		}
		return msgError;
	}

}
