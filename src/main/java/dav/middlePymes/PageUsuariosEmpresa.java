package dav.middlePymes;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.transversal.DatosEmpresarial;
//import dxc.execution.Evidence;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageUsuariosEmpresa extends PageInicioMiddle {

	// LOCATORS Y EXPRESIONES PARA ENCONTRAR LOS OBJETOS EN M�DULO USUARIOS EMPRESA
	// MIDDLE PYME
	By locClientEmp = By.id("cphCuerpo_txtBuscarNClienteEmpresarial");
	By locTipDocUser = By.id("cphCuerpo_dropBuscarTipoIdentificacion");
	By locNumDocUser = By.id("cphCuerpo_txtBuscarNumeroIdentificacion");
	String xPathLnkEditar = "//td[text()='(NUMID_USER)']//parent::tr//a[text()= 'Editar']";
	By locSeleEmpresa = By.id("cphCuerpo_dropEmpresa");
	By locRbOperIlim = By.id("cphCuerpo_rblDiasOperacionIlimitados");
	By locRbHorOpeIli = By.id("cphCuerpo_rblHorariosOperacionIlimitados");
	By locSelecAcceApp = By.id("cphCuerpo_dropCallCenter");
//	By locRbSelTodSer = By.id("cphCuerpo_rbtlAsignacionServicioTodosLosServicio");
	By locRbSelTodSerIlimitado = By.id("cphCuerpo_rbtlAsignacionServicioLimitado");
	String locSelecionaServicio = "//table[@id='cphCuerpo_gvServicio']//td[contains(text(), '(SERV)')]//parent::tr//td[1]//span//input";
	String locSelecionaIlimitadoSer = "//table[@id='cphCuerpo_gvServicio']//td[contains(text(), '(SERV)')]//parent::tr//td//table[@class]//tr//td//input[@value='Ilimitado']";
	By locPermisos = By.id("cphCuerpo_dropPermisos");
	By locRetSinTar = By.xpath("//*[@id='cphCuerpo_cblRetirosSinTarjeta_0']");
	String xPathCkAproPagos = "//input[@id='cphCuerpo_chblPermisos_0']";
	String xPathCkCreaPagos = "//input[@id='cphCuerpo_chblPermisos_1']";
	By locTableProd = By.id("cphCuerpo_gvProductos");
	String xPathCkProgRet = "//input[@id='cphCuerpo_cblRetirosSinTarjeta_0']";
	String xPathCkSelTodPro = "//input[@id='cphCuerpo_chkProductoAutorizado']";
	By locConfirTok = By.id("cphCuerpo_txtConfirmarToken");

	static String datoTokMiddle = null;
	private static int contador = 1;

	public PageUsuariosEmpresa(PageLoginPymes parentPage) {
		super(parentPage);
	}

	public static String[] datosMidellToke(String datoTok) {
		datoTokMiddle = datoTok;
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite actualizar el usuario con el fin de que se asigne las
	 * novedades que se han realizado sobre sus servicios<br>
	 * Retorna mensaje de error no fue exitosa la actualizaci�n del usuario DLC es
	 * null.
	 */

	public String actualizarUsuarioEmpresa(String clientEmp, String tipDocUser, String numDocUser, String nomEmpre)
			throws Exception {
		DXCUtil.wait(2);
		this.irAOpcion("Administración de Usuarios", "Administración", "Usuarios Empresas");
		WebElement objClientEmp;

		do {
			DXCUtil.wait(1);
			objClientEmp = this.element(locClientEmp);
		} while (!this.isDisplayed(locClientEmp));
		this.write(objClientEmp, clientEmp);
		this.selectListItem(locTipDocUser, tipDocUser);
		DXCUtil.wait(1);
		this.write(locNumDocUser, numDocUser);
		DXCUtil.wait(1);
		this.clickButton("Buscar");
		DXCUtil.wait(1);
		// SE REEMPLAZA DENTRO DEL XPATH [(NUMID_USER)]POR EL NUMERO DE IDENTIFICACI�N
		// DEL USUARIO PARA REALIZAR LA BUSQUEDA
		String path = xPathLnkEditar.replace("(NUMID_USER)", numDocUser);
		WebElement objBusEmp, objTablePro, objCajToken;
		String msgAlerta = null;

		List<WebElement> objSelEditar, objRbOperIli, objRbHorOper, objRbTodSer, objCkAproPag, objCkCrePag, objRetTarj,
				objCkProgPag, objCkSelTodPro;

		// SE PRESENTA QUE RETORNE LA GRILLA CON LA INFORMACI�N CONSULTADA SINO
		// ENCUENTRA INFOMACION RETORNA MENSAJE DE ALERTA.
		do {
			DXCUtil.wait(1);
			objSelEditar = this.findElements(By.xpath(path));
			if (objSelEditar == null) {
				DXCUtil.wait(1);
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			}
		} while (!this.isDisplayed(objSelEditar.get(0)) && msgAlerta == null);
//		Evidence.save("busquedaUsu[" + clientEmp + "_" + numDocUser + "]", this);
		Evidence.save("busquedaUsu[" + clientEmp + "_" + numDocUser + "]");
		// SI SE PRESENTA LA INFORMACI�N EN LA GRILLA BUSCA EL DATO DEL USUARIO Y E DA
		// CLIC EN EDITAR.
		if (msgAlerta != null)
			return msgAlerta;
		this.click(objSelEditar.get(0));
		// ESPERA HASTA QUE SE PRESENTE EL CAMPO DE SELECCIONAR EMPRESA.
		do {
			DXCUtil.wait(1);
			objBusEmp = this.element(locSeleEmpresa);
		} while (objBusEmp == null);
		
		String[] empresasContra = this.getListItems(locSeleEmpresa);

		if (nomEmpre.equals("MANTENIMINETO DE EQUIPOS DE SEGURI")|| nomEmpre.equals("MANTENIMINETO DE EQUIPOS DE SE GURIDAD")|| nomEmpre.equals("MANTENIMINETO DE EQUIPOS DE SEGUIR")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("MANTENIMINETO DE EQUIPOS DE SEGURI")|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SE GURIDAD")|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SEGUIR")|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SE GUR")) {
					nomEmpre = empresas;

				}
			}
		}
		
		if (nomEmpre.equals("EMP AUTO DOS")|| nomEmpre.equals("EMP PYME AUTO DOS")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("EMP AUTO DOS")|| empresas.contains("EMP PYME AUTO DOS")) {
					nomEmpre = empresas;

				}
			}
		}

		if (nomEmpre.equals("CAMILO QUEVEDO QUEVEDO")|| nomEmpre.equals("CAMILO TORRES")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("CAMILO QUEVEDO QUEVEDO")|| empresas.contains("CAMILO TORRES")) {
					nomEmpre = empresas;

				}
			}
		}
		
		msgAlerta = this.selectListItem(locSeleEmpresa, nomEmpre);
		if (!msgAlerta.isEmpty())
			return msgAlerta;
		DXCUtil.wait(3);
		objRbOperIli = this.findElements(locRbOperIlim);
		if (!objRbOperIli.get(0).isSelected())
			this.click(objRbOperIli.get(0));
		DXCUtil.wait(2);
		objRbHorOper = this.findElements(locRbHorOpeIli);
		if (!objRbHorOper.get(0).isSelected())
			this.click(objRbHorOper.get(0));
		DXCUtil.wait(2);
		this.selectListItem(locSelecAcceApp, "Si");
		DXCUtil.wait(2);

//		objRbTodSer = this.findElements(locRbSelTodSer);
//		if (!objRbTodSer.get(0).isSelected())
//			this.click(objRbTodSer.get(0));
//		DXCUtil.wait(2);

		objRbTodSer = this.findElements(locRbSelTodSerIlimitado);
		if (!objRbTodSer.get(0).isSelected())
			this.click(objRbTodSer.get(0));
		DXCUtil.wait(2);

		String servicio = "";
		
		if (SettingsRun.getTestData().parameterExist("Servicio")) {
			servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		}else {
			servicio = "Consultas de Productos";
		}
		
		String servicico = getNbLinkServicio(servicio);
		if (!servicio.equals("Atencion en linea") && !servicio.equals("Retiro sin Tarjeta")) {
			// SE REEMPLAZA EN XPATH EL NUMERO DE EMPLEADO Y EL CLIENTE EMPRESARIAL.
			String pathx = locSelecionaServicio.replace("(SERV)", servicico);
			List<WebElement> objResultado;
			// ESPERA QUE SE PRESENTE LA GRILLA CON LA INFORMACI�N DE BUSQUEDA O MENSAJE DE
			// ALERTA.
			Evidence.saveAllScreens("resultadoBusqueda1", this);
			do {
				DXCUtil.wait(1);
				contador++;
				objResultado = this.findElements(By.xpath(pathx));
				if (objResultado.isEmpty()) {
					msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
				} else if (objResultado.size() > 1) {
					msgAlerta = "ERROR -- Hay varios elementos duplicados en la busqueda, se continua aun con el flujo.";
//				return msgAlerta;
				}
				if (contador >= 30) {
					return "TimeOut no se presento Alerta o Resultado de la busqueda";
				}
			} while (objResultado.isEmpty());

			if (!objResultado.get(0).isSelected()) {
				this.click(objResultado.get(0));

			}

			DXCUtil.wait(2);
			if (!servicico.equals("Consultas de Productos")) {
			String pathxIlim = locSelecionaIlimitadoSer.replace("(SERV)", servicico);
			List<WebElement> objResultado2;
			// ESPERA QUE SE PRESENTE LA GRILLA CON LA INFORMACI�N DE BUSQUEDA O MENSAJE DE
			// ALERTA.
//		Evidence.save("resultadoBusquedaiMP", this);
			Evidence.save("resultadoBusquedaiMP");
			if (!servicico.equals("Oficina Virtual") && !servicico.equals("Libranza")) {
				do {
					objResultado2 = this.findElements(By.xpath(pathxIlim));
					DXCUtil.wait(1);
					if (objResultado2.isEmpty()) {
						msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
					} else if (objResultado2.size() > 1) {
						msgAlerta = "ERROR -- Hay varios elementos duplicados en la busqueda, se continua aun con el flujo.";
//				return msgAlerta;
					}
				} while (objResultado2.isEmpty());

				this.click(objResultado2.get(0));
				DXCUtil.wait(1);
			}
		}
		}
		this.selectListItem(locPermisos, "Pagos y Transferencias");

//***********************************************************************************************************************
		int contador = 1;
		// ESPERA HASTA QUE SE PRESENTE EL TABLA DE TODOS LOS PRODUCTOS
		do {
			DXCUtil.wait(1);
//			objTablePro = this.element(locTableProd);
			contador++;
			if (contador > 30) {
				msgAlerta = "No se presento la tabla de todos los Productos";
				return msgAlerta;
			}

		} while (this.element(locTableProd) == null);

		// SE REALIZA LA VALIDACION SI LOS CHECKS ESTAN SELECCIONADOS SINO REALIZA LA
		// MARCACI�N.
		objCkAproPag = this.findElements(By.xpath(xPathCkAproPagos));
		if (!objCkAproPag.get(0).isSelected())
			this.click(objCkAproPag.get(0));

		DXCUtil.wait(2);

		objCkCrePag = this.findElements(By.xpath(xPathCkCreaPagos));
		if (!objCkCrePag.get(0).isSelected())
			this.click(objCkCrePag.get(0));

		DXCUtil.wait(2);

		objRetTarj = this.findElements(locRetSinTar);
		if (!objRetTarj.get(0).isSelected())
			this.click(locRetSinTar);

		DXCUtil.wait(2);

		// Todos los produtos
		objCkSelTodPro = this.findElements(By.xpath(xPathCkSelTodPro));
		if (!objCkSelTodPro.get(0).isSelected())
			this.click(objCkSelTodPro.get(0));

		this.contratarConsultasProductos();
//***********************************************************************************************************************

		// INGRESA LA DINAMICA Y MIENTAS NO SE PRESENTE LA ALERTA, VUELVE A INGRESAR LA
		// DINAMICA Y DA CLIC EN ACTUALIZAR.
		String token = datoTokMiddle;
//		Evidence.save("PermisosUsuario[" + nomEmpre + "]", this);
		Evidence.save("PermisosUsuario[" + nomEmpre + "]");
		objCajToken = this.element(locConfirTok);
		do {
			DXCUtil.wait(1);
		} while (this.element(locConfirTok) == null);
		this.focus(objCajToken);
		this.write(objCajToken, token);
		Evidence.saveFullPage("NUMTokenclave", this);
		this.clickButton("Actualizar");

		do {
			DXCUtil.wait(1);
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			Evidence.saveFullPage("ActualizaciónUsuarioMsg", this);
		} while (msgAlerta == null);
//		Evidence.save(msgAlerta, this);
		Evidence.save(msgAlerta);
		this.clickButton("Volver");
		return msgAlerta;
	}

	// ***********************************************************************************************************************
	/**
	 * Este metodo contrata el servicio de consulta de productos
	 * 
	 * @throws Exception
	 */
	public void contratarConsultasProductos() throws Exception {

		String msgAlerta = null;
		String servicio = "Consultas de Productos";

		String Servicico = getNbLinkServicio(servicio);
		if (!servicio.equals("Atencion en linea")) {
			// SE REEMPLAZA EN XPATH EL NUMERO DE EMPLEADO Y EL CLIENTE EMPRESARIAL.
			String pathx = locSelecionaServicio.replace("(SERV)", Servicico);
			List<WebElement> objResultado;
			// ESPERA QUE SE PRESENTE LA GRILLA CON LA INFORMACI�N DE BUSQUEDA O MENSAJE DE
			// ALERTA.
			Evidence.saveAllScreens("resultadoBusqueda1", this);
			do {
				objResultado = this.findElements(By.xpath(pathx));
				DXCUtil.wait(1);
				if (objResultado.isEmpty()) {
					msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
				} else if (objResultado.size() > 1) {
					msgAlerta = "ERROR -- Hay varios elementos duplicados en la busqueda, se continua aun con el flujo.";
//					return msgAlerta;
				}
			} while (objResultado.isEmpty());

			if (!objResultado.get(0).isSelected()) {
				this.click(objResultado.get(0));

			}
		}
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite validar que exista la asociaci�n entre cliente
	 * empresarial y el usuario<br>
	 * Retorna [msgAlerta] si no existe el usuario. Retorna NULL si existe el
	 * usuario.
	 * 
	 * @throws Exception
	 */

	public String asoClienUser(String cliEmp, String tipIdUser, String numIdUse) throws Exception {
		DXCUtil.wait(2);
		this.irAOpcion("Administración de Usuarios", "Administración", "Usuarios Empresas");
		WebElement objClientEmp;
		// SE ESPERA QUE SE PRESENTE EL CAMPO CLIENTE EMPRESARIAL
		do {
			objClientEmp = this.element(locClientEmp);
		} while (objClientEmp == null);
		// SE INGRESAN LOS DATOS Y SE DA CLIC EN EL BOTON BUSCAR
		this.write(objClientEmp, cliEmp);
		this.selectListItem(locTipDocUser, tipIdUser);
		this.write(locNumDocUser, numIdUse);
		this.clickButton("Buscar");
		DXCUtil.wait(1);
		// SE REEMPLAZA DENTRO DEL XPATH [(NUMID_USER)]POR EL NUMERO DE IDENTIFICACI�N
		// DEL USUARIO PARA REALIZAR LA BUSQUEDA
		String path = xPathLnkEditar.replace("(NUMID_USER)", numIdUse);
		WebElement objSelEditar;
		String msgAlerta = null;
		// SE ESPERE QUE SE PRESENTE LA GRILLA CON LA INFORMACI�N CONSULTADA SINO
		// ENCUENTRA INFOMACION RETORNA MENSAJE DE ALERTA.
		do {
			objSelEditar = this.element(path);
			if (objSelEditar == null)
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");

		} while (objSelEditar == null && msgAlerta == null);
//		Evidence.save("busquedaUsuario", this);
		Evidence.save("busquedaUsuario");
		// SI SE PRESENTA LA INFORMACI�N EN LA GRILLA BUSCA EL DATO DEL USUARIO
		if (msgAlerta != null) {
//			Evidence.save(msgAlerta, this);
			Evidence.save(msgAlerta);
			return msgAlerta;
		}
//		Evidence.save("ResultadoBuesqueda", this);
		Evidence.save("ResultadoBuesqueda");
		// SI LLEGA A ESTE PUNTO ES PORQUE EXISTE EL LA ASOCIACI�N DEL CLIENTE CON EL
		// USUARIO.
		return null;
	}

	/**
	 * Retorna el nombre del link del sevicio tal como se presenta en el Portal.<br>
	 * Si el [servicio] no es uno delos que se esperan, retorna "NO HAY LINK
	 * DISPONIBLE".
	 */
	public String getNbLinkServicio(String servicio) {

		String serv = servicio;

		String nbServicio = "NO HAY LINK DISPONIBLE";

		if (serv.contains("Nómina"))
			nbServicio = "Pago de Nóminas";

		else if (serv.contains("Proveedores"))
			nbServicio = "Pago a Proveedores";

		else if (serv.contains("AFC"))
			nbServicio = "Pagos a cuentas AFC";

		else if (serv.contains("Crédito.3ros"))
			nbServicio = "Pagos a créditos de terceros";

		else if (serv.contains("Propios"))
			nbServicio = "Pagos Propios";

		else if (serv.contains("Servicios"))
			nbServicio = "Pago de Servicios";

		else if (serv.contains("Pagos Automaticos"))
			nbServicio = "Pago Automático de Servicios Públicos o Privados";

		else if (serv.contains("Cuenta No Inscrita"))
			nbServicio = "Transferencias Cuenta No Inscrita";

		else if (serv.contains("Cuenta Inscrita"))
			nbServicio = "Transferencias Cuenta Inscrita";

		else if (serv.contains("NIT"))
			nbServicio = "Transferencias Mismo NIT";

		else if (serv.contains("Internacionales"))
			nbServicio = "Transferencias Internacionales";

		else if (serv.contains("Consultas de Productos"))
			nbServicio = "Consultas de Productos";

		else if (serv.contains("Oficina Virtual"))
			nbServicio = "Oficina Virtual";

		else if (serv.contains("Libranza"))
			nbServicio = "Libranza";

		else if (serv.contains("Comercios"))
			nbServicio = "Comercios";

		else if (serv.contains("Recaudo"))
			nbServicio = "Recaudo";
		
		else if (serv.contains("Inscripciones"))
			nbServicio = "Inscripciones";

		return nbServicio;
	}
}
