package dav.middlePymes;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageServicios extends PageInicioMiddle {

	public static final String MSG_CONTRA_EXITO = "SE GRABÓ EXITOSAMENTE EL SERVICIO";
	public static final String MSG_SERVI_CONTRA = "EL SERVICIO YA ESTA CONTRATADO";
	public static final String MSG_SERVI_NO_ENCONTRO = "NO SE ENCONTRO EL SERVICIO";

	private static int contador = 1;
	String msgConvenio = null;
	String msgConyaRegistrado = null;

	By locTipId = By.id("cphCuerpo_ddlBQTipoIdentificacionEmpresa");
	By locClieEmp = By.id("cphCuerpo_txtNoClienteEmpresa");
	By locNumEmp = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By locIdTabla = By.id("cphCuerpo_gvServicio");
	String xPathLnkContra = "//td[text()= '(NUM_EMP)']//following-sibling::td[text()= '(CE)']//parent::tr//a[text()= 'Contratación de Servicios']";
	String xPathServEst = "//td[contains(text(), '(SERV)')]//parent::tr//td[4]//select";
	String xPathServEdit = "//td[contains(text(), '(SERV)')]//parent::tr//td[3]//a[(text()='Editar')]";
	String xPathRbServ = "//td[contains(text(), '(SERV)')]//parent::tr//td[2]//span//input";
	String xPathSiguienteDis = "//a[@class='aspNetDisabled' and contains(text(),'Siguiente')]";
	By locSiguiente = By.id("cphCuerpo_lnkDSiguiente");
	By locEstado = By.id("cphCuerpo_dropEstadoServicio");
	By locTipProduct = By.id("cphCuerpo_ddlDETipoProducto");
	By locNumProduct = By.id("cphCuerpo_rfv_ddlDENumeroProducto"); // cphCuerpo_ddlDENumeroProducto
	String XpathCuenta = "//select[@id='cphCuerpo_rfv_ddlDENumeroProducto' or @id='cphCuerpo_ddlDENumeroProducto']";
	By locPrimero = By.id("cphCuerpo_lnkDPrimero");
	String xPathPrimerDisabled = "//a[@class='aspNetDisabled' and contains(text(),'Primero')]";
	By CodigoEmp = By.xpath("//*[@id='cphCuerpo_lbRDAVCodigoEmpresa']");
	By BaseDatos = By.xpath("//*[@id='cphCuerpo_ddlBDRecaudos']");
	By NombreEmpresaRecaudo = By.xpath("//*[@id='cphCuerpo_lbRDAVNombreEmpresa']");
	By EstructuraBD = By.xpath("//*[@id='cphCuerpo_ddlreca_tipoArchivo']");
	By Adicionar = By.xpath("//*[@id='cphCuerpo_lbRDAVAdicionar']");
	By Actualizar = By.xpath("//*[@id='cphCuerpo_btnGrabarServicio']");
	By tablaConvenio = By.xpath("//*[@id='cphCuerpo_pnRecaudo']/table/tbody/tr[7]/td");
	By OPlibranza = By.xpath("//*[@id=\"cphCuerpo_dropLibranza\"]");

	public PageServicios(PageLoginPymes parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite buscar y contratar el o los servicio. El tama�o del array
	 * = n� servicios enviados po parametro. En el array retorna mensaje de los
	 * servicios enviados por parametro en su orden con mensaje de contratacion o
	 * Error Retorna [n] = Error o servicio contratado. //puede retornar mensaje de
	 * error de conrataci�n en la primera posicion[] y serian null las siguientes
	 * posiciones[]
	 */
	@SuppressWarnings("unlikely-arg-type")
	public String[] contratarServicio(String clieEmp, String tipId, String numEmp, String... servicio)
			throws Exception {
		this.irAOpcion("Administración de Servicios", "Administración", "Servicios");
		WebElement objselId;
		String[] arrRetorno = new String[servicio.length];

		do {
			objselId = this.element(locTipId);
		} while (objselId == null);

		do {
			contador++;
			DXCUtil.wait(1);
			objselId = this.element(locTipId);
			if (contador >= 30) {
				arrRetorno[0] = "TimeOut no se presento el campo Tipo Identificación";
				return arrRetorno;
			}
		} while (objselId == null);

		this.selectListItem(locTipId, tipId);
		this.write(locNumEmp, numEmp);
		this.write(locClieEmp, clieEmp);
		this.clickButton("Buscar");
		DXCUtil.wait(2);
		// SE REEMPLAZA EN XPATH EL NUMERO DE EMPLEADO Y EL CLIENTE EMPRESARIAL.
		String path = xPathLnkContra.replace("(NUM_EMP)", numEmp).replace("(CE)", clieEmp);
		List<WebElement> objResultad;
		String msgAlerta = null;
		// ESPERA QUE SE PRESENTE LA GRILLA CON LA INFORMACI�N DE BUSQUEDA O MENSAJE DE
		// ALERTA.
		do {
			contador++;
			DXCUtil.wait(2);
			objResultad = this.findElements(By.xpath(path));
			if (objResultad.isEmpty()) {
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			} else if (objResultad.size() > 1) {
				Reporter.reportEvent(Reporter.MIC_INFO,
						"ERROR -- Hay varios elementos duplicados en la busqueda, se continua aun con el flujo.");
			}

			if (contador >= 30) {
				Evidence.saveFullPage("TimeOut no se presento Alerta o Resultado de la busqueda", this);
				arrRetorno[0] = "TimeOut no se presento Alerta o Resultado de la busqueda";
				return arrRetorno;
			}
		} while (objResultad.isEmpty() && msgAlerta == null);
//		Evidence.save("resultadoBusqueda", this);
		Evidence.save("resultadoBusqueda");
		// SE GUARDA EN LA POSICION [0] EL MENSAJE QUE SE PRESENTE.
		if (msgAlerta != null) {
			arrRetorno[0] = msgAlerta;
			return arrRetorno;
		}
		this.click(objResultad.get(0));
		// SE ESPERA QUE SE PRESENTE LA TABLA CON LOS SERVICIOS.
		do {
			DXCUtil.wait(1);
		} while (!this.isDisplayed(locIdTabla));
		this.contratarConsultas();
		for (int i = 0; i < servicio.length; i++) {
			if (!servicio[i].contains("Atencion en linea") && !servicio[i].contains("Retiro sin Tarjeta")) {

				if (this.buscarServicio(servicio[i])) {
					String pathEstado = xPathServEst.replace("(SERV)", servicio[i]);
					String pathEditar = xPathServEdit.replace("(SERV)", servicio[i]);

					List<WebElement> objVigencia = this.findElements(By.xpath(pathEstado));
					List<WebElement> objEditar = this.findElements(By.xpath(pathEditar));

					// CONDICION PARA VALIDAR SI YA ESTA CONTRATADO EL SERVICIO, SINO LO CONTRATA.
					if (objVigencia == null || objVigencia.isEmpty()) {
						// METODO DE CONTRATACI�N DE SERVICIO.
						String msgError = this.parametrizarServicio(servicio[i]);

						if (msgError == null)
							arrRetorno[i] = MSG_CONTRA_EXITO;
						else
							arrRetorno[i] = msgError;
						if (msgConvenio != null) {
							if (msgConvenio.contains("El número del convenio no está asignado a la empresa.")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										msgConvenio + " Ingrese Una empresa con convenio");
								SettingsRun.exitTestIteration();
							}

							if (msgConvenio.contains("Convenio ya registrado.")) {
								Reporter.reportEvent(Reporter.MIC_INFO,
										msgConvenio + " El convenio ya habia sido registrado");
							} else if (msgConvenio.contains("CLIENTE NO TIENE CONVENIOS 00")) {
								Reporter.reportEvent(Reporter.MIC_FAIL,
										msgConvenio + " Ingrese Una empresa con convenio");
								SettingsRun.exitTestIteration();
							}
						} else if (msgConvenio == null) {
							Reporter.reportEvent(Reporter.MIC_INFO, " El convenio ha sido registrado");
						}

					}

					else if (objVigencia != null || !objVigencia.isEmpty()) {
						// METODO DE CONTRATACI�N DE SERVICIO. {
						String vigencia = this.getItemSelected(objVigencia.get(0)).toUpperCase();
						arrRetorno[i] = MSG_SERVI_CONTRA;

						if (!vigencia.equals("VIGENTE")) {

							this.click(objEditar.get(0));
							WebElement objEstado;
							do {
								objEstado = this.element(locEstado);
							} while (objEstado == null);
							this.selectListItemExacto(objEstado, "Vigente");
							String titulo = this.getTitle();
							Evidence.saveFullPage("Vigencia", this);
							this.clickButton("Actualizar");
							// ESPERA A QUE SE MUESTRE RESULTADO O MENSAJE DE ALERTA.
							boolean existeDialog;

							// ESPERA QUE SE PRESENTE EL CUADRO DE DIALOGO Y LO ACEPTA.
							do {
								existeDialog = this.existDialog();
								DXCUtil.wait(1);
							} while (!existeDialog);
							String msgDialo = this.getMessageDialog();
							Evidence.saveWithDialog(msgDialo, this, titulo);
							this.acceptDialog();
							// EXTRAE EL MENSAJE DE ALERTA Y LO GUARDA EN EL ARRAY.
							do {
								msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
							} while (msgAlerta == null);
//							Evidence.save("ServicioContratado: " + servicio[i], this);
							Evidence.save("ServicioContratado: " + servicio[i]);
							arrRetorno[i] = MSG_CONTRA_EXITO;

						} else
							arrRetorno[i] = MSG_SERVI_CONTRA;
					}
				}
			} else if (servicio[i].contains("Atencion en linea") || servicio[i].contains("Retiro sin Tarjeta")) {
				arrRetorno[i] = MSG_SERVI_CONTRA;

			} else {
				arrRetorno[i] = MSG_SERVI_NO_ENCONTRO;
			}
		}
		// SE REALIZA LA NAVEGACI�N ENTRE SU PAGINACI�N.
		if (servicio[0].contains("Recaudo") && msgConvenio != null) {
			Evidence.saveFullPage("Vigencia", this);
			this.clickButton("Actualizar");
			// ESPERA A QUE SE MUESTRE RESULTADO O MENSAJE DE ALERTA.
			boolean existeDialog;

			// ESPERA QUE SE PRESENTE EL CUADRO DE DIALOGO Y LO ACEPTA.
			do {
				existeDialog = this.existDialog();
				DXCUtil.wait(1);
			} while (!existeDialog);
			String msgDialo = this.getMessageDialog();
			Evidence.save(msgDialo);
			this.acceptDialog();
			// EXTRAE EL MENSAJE DE ALERTA Y LO GUARDA EN EL ARRAY.
			do {
				msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			} while (msgAlerta == null);
//			Evidence.save("ServicioContratado: " + servicio[0], this);
			Evidence.save("ServicioContratado: " + servicio[0]);
			arrRetorno[0] = msgConvenio;

		} else if (servicio[0].contains("Recaudo") && msgConvenio == null) {
			Evidence.saveFullPage("Vigencia", this);
			String pathEstado = xPathServEst.replace("(SERV)", servicio[0]);
			List<WebElement> objVigencia = this.findElements(By.xpath(pathEstado));
			if (this.element(tablaConvenio) != null) {
				this.clickButton("Actualizar");
				// ESPERA A QUE SE MUESTRE RESULTADO O MENSAJE DE ALERTA.
				boolean existeDialog;

				// ESPERA QUE SE PRESENTE EL CUADRO DE DIALOGO Y LO ACEPTA.
				do {
					existeDialog = this.existDialog();
					DXCUtil.wait(1);
				} while (!existeDialog);
				String msgDialo = this.getMessageDialog();
				Evidence.save(msgDialo);
				this.acceptDialog();

				// EXTRAE EL MENSAJE DE ALERTA Y LO GUARDA EN EL ARRAY.
				do {
					msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
				} while (msgAlerta == null);
//				Evidence.save("ServicioContratado: " + servicio[0], this);
				Evidence.save("ServicioContratado: " + servicio[0]);
				arrRetorno[0] = msgConvenio;
			}
		}
		WebElement objSigDisable;
		objSigDisable = this.element(xPathSiguienteDis);
		if (objSigDisable != null)
			this.click(locPrimero);
		WebElement objsiguiente;
		do {
			objsiguiente = this.element(locSiguiente);
		} while (objsiguiente == null);

		Evidence.saveFullPage("servicios", this);
		DXCUtil.wait(2);
		this.click(locSiguiente);

		do {
			objSigDisable = this.element(xPathSiguienteDis);
		} while (objSigDisable == null);
		Evidence.saveFullPage("servicios", this);

		this.clickButton("Actualizar");
		do {
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (msgAlerta == null);
		this.moveUpScreen();
		Evidence.saveFullPage("ActualizacionServicio", this);
		DXCUtil.wait(1);
		return arrRetorno;

	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite buscar el servicio en la tabla si tiene paginaci�n.
	 * Retorna [true] si encuentra el servicio, sino retorna [false].
	 */

	public boolean buscarServicio(String servicio) throws Exception {

		WebElement siguiente;
		WebElement primerEnabled = this.element(xPathPrimerDisabled);

		if (primerEnabled == null)
			this.click(locPrimero);

		do {
			DXCUtil.wait(1);
			siguiente = this.element(locSiguiente);
		} while (siguiente == null); // ESPERA A QUE SE PRESENTE HABILITADO EL BOTON DE SIGUIENTE

		String path = xPathRbServ.replace("(SERV)", servicio);
		List<WebElement> objServicio;
		WebElement objSigDisable;

		objServicio = this.findElements(By.xpath(path));

		if (objServicio == null || objServicio.isEmpty()) {
			Evidence.saveFullPage(servicio, this);
			this.click(locSiguiente);

			// ESTA FUNCION REALIZA LA BUSQUEDA DEL SERVICIO ENTRE SUS PAGINAS, CUANDO EL
			// LINK SIGUIENTE SE DESHABILITE, TERMINA LA BUSQUEDA.
			do {
				DXCUtil.wait(1);
				objSigDisable = this.element(xPathSiguienteDis);
			} while (objSigDisable == null);

			Evidence.saveFullPage(servicio, this);
			objServicio = this.findElements(By.xpath(path));

			if (objServicio == null || objServicio.isEmpty())
				return false;
		}
		return true;

	}

//***********************************************************************************************************************
	/**
	 * Este metodo permite dar clic en el check del servicio para contratarlo,
	 * ingresa al mismo selecciona vigente <br>
	 * y en los casos que en el servicio toque asociar tipo y numero de cuenta, el
	 * metodo lo realiza de forma aleatoria. Retorna [null] si fue exitosa la
	 * contrataci�n, sino retorna mensaje con la falla.
	 */
	public String parametrizarServicio(String servicio) throws Exception {

		String path = xPathRbServ.replace("(SERV)", servicio);
		List<WebElement> objAsignar = this.findElements(By.xpath(path));
		this.click(objAsignar.get(0));
		WebElement objEstado;
		if (servicio.contentEquals("Libranza")) {
			this.selectListItemRandom(OPlibranza, true);
			Evidence.save("Contratación Libranza");
		} else if (!servicio.contentEquals("Libranza")) {
			// ESPERA QUE SE PRESENTE LA LISTA ESTADO DEL SERVICIO
			do {
				DXCUtil.wait(1);
				objEstado = this.element(locEstado);
			} while (objEstado == null);

			String msg = this.seleOptionIgual(locEstado, "Vigente");

			if (msg.isEmpty()) {
				return "Estado del Servicio No en contrado";
			}
			DXCUtil.wait(3);
		}

		WebElement objTipoCuenta = this.element(locTipProduct);

		// Asignacion Recaudos
		if (servicio.equals("Recaudo")) {
			String CodigoConv = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
			String BaseD = SettingsRun.getTestData().getParameter("Base de Datos").trim();
			String NomEMP = SettingsRun.getTestData().getParameter("Nombre de la empresa para publicar").trim();
			String Estructura = SettingsRun.getTestData().getParameter("Estructura").trim();
			this.selectListItem(BaseDatos, BaseD);
			Evidence.save("Recaudo Parametrizado");
			if (BaseD.contentEquals("SI")) {
				DXCUtil.wait(2);
				if (this.element(CodigoEmp) != null) {
					this.write(CodigoEmp, CodigoConv);
					this.write(NombreEmpresaRecaudo, NomEMP);
					this.selectListItem(EstructuraBD, Estructura);
					Evidence.save("Recaudo Parametrizado");
					click(Adicionar);
					int cont = 0;
					do {
						cont++;
						DXCUtil.wait(2);
						msgConvenio = getMsgAlertIfExist("lblMasterAlerta");
					} while (msgConvenio == null && cont <= 2);
					if (msgConvenio != null) {
						Evidence.save("Recaudo Parametrizado");
						return msgConvenio;
					}
					if (msgConvenio == null) {
						Evidence.save("Convenio Agregado");
						return msgConvenio;
					}
				}

			} else if (BaseD.contentEquals("NO")) {
				DXCUtil.wait(2);
				Evidence.save("Recaudo Parametrizado");
			}

		}

		// EN LOS SERVICIOS QUE DEBE ASIGNAR TIPO DE CUENTA Y NUMERO DE CUENTA, ESPERA
		// QUE SE HABILITEN LOS CAMPOS Y SELECCIONA ALEATORIAMENTE.
		if (objTipoCuenta != null) {
			this.selectListItemRandom(objTipoCuenta, false);
			DXCUtil.wait(2);
			WebElement objNumCuenta = this.element(XpathCuenta);
			this.selectListItemRandom(objNumCuenta, false);
		}
		Evidence.saveFullPage(servicio, this);
		this.clickButton("Actualizar");
		boolean existeDialog;

		String msgAlerta;
		// ESPERA QUE SE PRESENTE EL CUADRO DE DIALOGO Y LO ACEPTA.
		do {
//			DXCUtil.Movercursor();
			existeDialog = this.existDialog();
			DXCUtil.wait(1);
		} while (!existeDialog);
		this.getMessageDialog();
		this.acceptDialog();
		// ESPERA QUE SE PRESENTE EL MENSAJE DE ALERTA.
		do {
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (msgAlerta == null);
		if (!msgAlerta.toUpperCase().contains(MSG_CONTRA_EXITO))
			return msgAlerta;
		Evidence.saveFullPage("ServicioContratado:[" + servicio + "]", this);
		// ESPERA QUE SE PRESENTE LA TABLA CON LOS SERVICIOS.
		WebElement objTable;
		do {
//				DXCUtil.Movercursor();
			objTable = this.element(locIdTabla);
		} while (!objTable.isDisplayed());

		return null;
	}

	private void contratarConsultas() throws Exception {
		String servConsultas = "Consultas de Productos";
		String pathEst = xPathServEst.replace("(SERV)", servConsultas);
		String pathEdi = xPathServEdit.replace("(SERV)", servConsultas);
		List<WebElement> objVigenci = this.findElements(By.xpath(pathEst));
		List<WebElement> objEdita = this.findElements(By.xpath(pathEdi));

		// CONDICION PARA VALIDAR SI YA ESTA CONTRATADO EL SERVICIO, SINO LO CONTRATA.
		if (objVigenci == null || objVigenci.isEmpty()) {
			// METODO DE CONTRATACI�N DE SERVICIO.
			this.parametrizarServicio(servConsultas);
		} else if (objVigenci != null || !objVigenci.isEmpty()) {
			String vigen = this.getItemSelected(objVigenci.get(0)).toUpperCase();
			if (!vigen.equals("VIGENTE")) {
				this.click(objEdita.get(0));
				WebElement objEsta;
				do {
//					DXCUtil.Movercursor();
					objEsta = this.element(locEstado);
				} while (objEsta == null);
				this.selectListItemExacto(objEsta, "Vigente");
				Evidence.saveFullPage("Vigencia", this);
				this.clickButton("Actualizar");
				// ESPERA A QUE SE MUESTRE RESULTADO O MENSAJE DE ALERTA.
				boolean existeDialog;

				// ESPERA QUE SE PRESENTE EL CUADRO DE DIALOGO Y LO ACEPTA.
				do {
					existeDialog = this.existDialog();
					DXCUtil.wait(1);
				} while (!existeDialog);
				this.acceptDialog();
				// EXTRAE EL MENSAJE DE ALERTA Y LO GUARDA EN EL ARRAY.
				String msgAlerta;
				do {
//						DXCUtil.Movercursor();
					msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
				} while (msgAlerta == null);
//				Evidence.save("ServicioContratado: " + servConsultas, this);
				Evidence.save("ServicioContratado: " + servConsultas);
			}
		}
	}

// ***********************************************************************************************************************
	/**
	 * Retorna el nombre del link del sevicio tal como se presenta en el Portal.<br>
	 * Si el [servicio] no es uno delos que se esperan, retorna "NO HAY LINK
	 * DISPONIBLE".
	 */
	public static String getNbLinkServicio(String servicio) {

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

		else if (serv.contains("NIT") || serv.contains("Avance TC"))
			nbServicio = "Transferencia NIT Propio";

		else if (serv.contains("Internacionales"))
			nbServicio = "Transferencias Internacionales";

		else if (serv.contains("Consultas de Productos"))
			nbServicio = "Consultas de Productos";

		else if (serv.contains("Libranza"))
			nbServicio = "Libranza";

		else if (serv.contains("Oficina Virtual"))
			nbServicio = "Oficina Virtual";
		else if (serv.contains("Atencion en linea"))
			nbServicio = "Atencion en linea";

		else if (serv.contains("Comercios"))
			nbServicio = "Comercios";

		else if (serv.contains("Recaudo"))
			nbServicio = "Recaudo";

		else if (serv.contains("Retiro sin Tarjeta"))
			nbServicio = "Retiro sin Tarjeta";

		return nbServicio;
	}
}
