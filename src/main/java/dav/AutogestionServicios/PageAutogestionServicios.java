package dav.AutogestionServicios;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.transversal.NotificacionSMS;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dav.pymes.PageLoginPymes;

public class PageAutogestionServicios extends BasePageWeb {

	public PageAutogestionServicios(PageLoginPymes parentPage) {
		super(parentPage);
	}

// =======================================================================================================================
	// Page & Crontroller
	PageOrigen pageOrigen = null;
	PageConfirmacion pageConfirmacion = null;
	NotificacionSMS notificacionesSMS = null;
	PagePortalPymes pagePortalPymes = null;

	String servicio, tipoPrueba, cantidadServicios;

	String txDate;

	// Permite recolectar la información de los servicios que aprobo tx
	List<HashMap<String, String>> results = new ArrayList<>();

	// -----------------------------------------------------LOCATORS
	// FRONT----------------------------------------------------------

	// LOCATORS Tabla para consultar los servicios contratados para reemplazar
	String xpathServiciosContratados = "//*[@id='cphCuerpo_pnlResultadoBusqueda']/table/tbody//span[contains(text(),'SERVICIOS')]";
	String xpathServiciosContratadosCheckbox = "//*[@id='cphCuerpo_pnlResultadoBusqueda']/table/tbody//span[contains(text(),'SERVICIOS')]/../div/input[@type='checkbox']";

	// LOCATORS Tabla para consultar los servicios no contratados para reemplazar
	String xpathServiciosSinContratar = "//*[@id='cphCuerpo_pnlServiciosNOContratados']/table/tbody//span[contains(text(),'SERVICIOS')]";
	String xpathServiciosSinContratarCheckbox = "//*[@id='cphCuerpo_pnlServiciosNOContratados']/table/tbody//span[contains(text(),'SERVICIOS')]/../span/input[@type='checkbox']";

	// Locator para localizar el tooltip de cada servicio
	String xpathTooltip = "//span[contains(text(), 'SERVICIOS')]/../../../td/div";
	String xpathTooltipServicio = "//span[contains(text(), 'SERVICIOS')]/../../../td/div/span/div";

	// Locators tabla administración de usuarios
	By tablaAdmiUsuarios = By.xpath("//*[@id='cphCuerpo_gvUsuarios']/tbody");
	By selectEmpresa = By.id("cphCuerpo_dropEmpresa");
	By selectPermisos = By.id("cphCuerpo_dropPermisos");
	By btnSiguienteRg = By.id("cphCuerpo_lnkSiguiente");

	// Locator aprobar
	// tx----------------------------------------------------------------------
	By btnGuardar = By.id("cphCuerpo_btnGrabar");
	By aprovPagosTransfer = By.id("cphCuerpo_chblPermisos_0");
	By creacionPagosTransfer = By.id("cphCuerpo_chblPermisos_1");
	By programRetiSinTarj = By.id("cphCuerpo_cblRetirosSinTarjeta_0");
	By btnAprobarTx = By.id("cphCuerpo_btnAprobar");
	// Locator cancelar
	// tx-------------------------------------------------------------------------------
	By btnCancelarTx = By.id("cphCuerpo_btnCancelar");

	// Locators Para autorizar los servicios contratados
	String xpathAutorizarServicio = "//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'SERVICIOS')]/../td/span/input[@type='checkbox']";

	int contador = 0;

	// Metódo que permite seleccionar el servicio que se eliminara o contratara
	// segun la información del excel
	public List<HashMap<String, String>> contratarEliminarServicios() throws Exception {
		this.accesoRutaFront();

		this.cantidadServicios = SettingsRun.getTestData().getParameter("Cantidad servicios").trim();
		int cantidad = Integer.parseInt(this.cantidadServicios);
		DXCUtil.wait(2);
		Reporter.reportEvent(Reporter.MIC_INFO, "Se realizara la acción a " + cantidad + " servicios.");

		do {
			DXCUtil.wait(1);
			if (contador != cantidad) { // AUMENTA EL VALOR DE LA VARIABLE
				contador++;
			}
			boolean isServiceProcessedCorrectly = false;

			this.servicio = SettingsRun.getTestData().getParameter("Servicio " + contador).trim();
			this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo de prueba " + contador).trim();
			// Implementos para eliminar servicios contratados
			String xpathDelete = xpathServiciosContratados.replace("SERVICIOS", this.servicio);
			By xpathDeleteService = By.xpath(xpathDelete);
			// Xpath para darle click al checkbox de eliminar servicio
			String xpathInputDelete = xpathServiciosContratadosCheckbox.replace("SERVICIOS", this.servicio);

			// Implementos para contratar servicios sin contratar
			String xpathHire = xpathServiciosSinContratar.replace("SERVICIOS", this.servicio);
			By xpathHireService = By.xpath(xpathHire);
			// Xpath para darle click al checkbox de contratar servicio
			String xpathInputHire = xpathServiciosSinContratarCheckbox.replace("SERVICIOS", this.servicio);

			// Revisar el tooltip que se debe visualizar
			String xpathShowTooltip = xpathTooltip.replace("SERVICIOS", this.servicio);
			String xpathTextTooltip = xpathTooltipServicio.replace("SERVICIOS", this.servicio);

			// Elementos que nos poermitira visualizar el tooltip del servicio seleccionado
			WebElement isTooltip = this.findElement(By.xpath(xpathShowTooltip));
			WebElement isTextTooltip = this.findElement(By.xpath(xpathTextTooltip));

			// se declara action para poder realizar un movimiento de mouse, se usara para
			// que se ponga sobre cada tooltip
			Actions action = new Actions(this.getDriver());

			if (this.tipoPrueba.equals("Eliminar")) {
				try {

					// Este elemento buscara el servicio que se va a eliminar
					WebElement isHireService = this.findElement(xpathDeleteService);
					boolean inInputDeleteDisabled = this.findElement(By.xpath(xpathInputDelete))
							.getAttribute("disabled") != null;

					// Este elemento buscara el tooltip del servicio seleccionado

					if (isHireService.isEnabled() && !inInputDeleteDisabled) {
						DXCUtil.wait(1);
						this.click(By.xpath(xpathInputDelete));
						DXCUtil.wait(4);
						this.focus(isTooltip);

						if (isTooltip.isDisplayed() && isTextTooltip.isDisplayed()) {
							action.moveToElement(isTooltip).perform();
							Evidence.save("Servicios seleccionados");
							Reporter.reportEvent(Reporter.MIC_PASS, "Se ha seleccionado el servicio: " + this.servicio);
							Reporter.reportEvent(Reporter.MIC_PASS, "Información del tooltip de servicio: "
									+ this.servicio + ": " + isTextTooltip.getText());
							DXCUtil.wait(3);
							isServiceProcessedCorrectly = true; // Se procesó correctamente
						} else {
							Reporter.reportEvent(Reporter.MIC_WARNING,
									"No se encontro un tooltip, verifique la información y reportelo.");
						}

					}

				} catch (Exception e) {
					Reporter.reportEvent(Reporter.MIC_WARNING, "El servicio (" + this.servicio
							+ ") no se encuentra contratado o está deshabilitado para seleccionar, verifique la información.");
					DXCUtil.wait(4);
					action.moveToElement(isTooltip).perform();
					Evidence.save("Servicios seleccionados");
					Reporter.reportEvent(Reporter.MIC_DONE,
							"Tooltip del servicio " + this.servicio + ": " + isTextTooltip.getText());

				}
			} else if (this.tipoPrueba.equals("Contratar")) {
				try {
					// Este elemento buscara el servicio que se va a contratar
					WebElement isServiceWoHire = this.findElement(xpathHireService);
					boolean inInputHireDisabled = this.findElement(By.xpath(xpathInputHire))
							.getAttribute("disabled") != null;

					// Este elemento buscara el tooltip del servicio seleccionado
					if (isServiceWoHire.isDisplayed() && !inInputHireDisabled) {
						this.click(By.xpath(xpathInputHire));
						DXCUtil.wait(4);
						this.focus(isTooltip);

						if (isTooltip.isDisplayed() && isTextTooltip.isDisplayed()) {
							action.moveToElement(isTooltip).perform();
							Evidence.save("Servicios seleccionados");
							Reporter.reportEvent(Reporter.MIC_PASS, "Se ha seleccionado el servicio: " + this.servicio);
							Reporter.reportEvent(Reporter.MIC_PASS, "Información del tooltip de servicio: "
									+ this.servicio + ": " + isTextTooltip.getText());
							DXCUtil.wait(3);
							isServiceProcessedCorrectly = true; // Se procesó correctamente
						} else {
							Reporter.reportEvent(Reporter.MIC_FAIL,
									"No se encontro un tooltip, verifique la información y reportelo.");
						}

					}

				} catch (Exception e) {
					Reporter.reportEvent(Reporter.MIC_WARNING, "El servicio (" + this.servicio
							+ ") no se encuentra contratado o está deshabilitado para seleccionar, verifique la información.");
					DXCUtil.wait(4);
					this.focus(isTooltip);
					action.moveToElement(isTooltip).perform();
					Evidence.save("Servicios seleccionados");
					Reporter.reportEvent(Reporter.MIC_PASS,
							"Tooltip del servicio: " + this.servicio + ": " + isTextTooltip.getText());
				}
			}
			try {
				// Verificara que se agregen los servicios y el tipo de prueba aprobados al
				// HashMap
				if (isServiceProcessedCorrectly != false) {
					HashMap<String, String> servicioActual = new HashMap<String, String>();
					servicioActual.put("servicioAprobado", this.servicio);
					servicioActual.put("tipoPruebaAprobada", this.tipoPrueba);
					results.add(servicioActual);

				}

			} catch (Exception e) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"No se guardo correctamente, comuniquese con un automatizador.");
			}

		} while (contador < cantidad);

		if (!results.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"Se han almacenado los servicios seleccionados correctamente: " + results);
		} else {
			Reporter.reportEvent(Reporter.MIC_WARNING,
					"No se guardaron procesos porque no se seleccionó ningun servicio, verifique la información.");
			// Permite validar la visibilidad del texto y la url con las tarifas
		}

		String aprobarTx = SettingsRun.getTestData().getParameter("Aprobar Tx");
		if (aprobarTx.equals("Aprobar") && !results.isEmpty()) {
			DXCUtil.wait(1);
			this.click(btnAprobarTx);
			Evidence.save("Aprobar Tx");
			this.aprobarTransaccion();
			DXCUtil.wait(2);
			Date dateTx = this.pageConfirmacion.getFechaHoraTx();
			// Se obtiene la fecha, si es nula no pasara al portal Middle, de lo contrario
			// si ira al portal
			if (dateTx != null) {
				DateFormat dateTxFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				txDate = dateTxFormat.format(dateTx);
				Reporter.reportEvent(Reporter.MIC_PASS, "Se aprobó la transacción.");
				// Permite validar la visibilidad del texto y la url con las tarifas
				this.tarifas();
				this.autorizarServicios(results);
			} else if (dateTx == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontro ninguna transacción, por favor verifique los servicios.");
				// Permite validar la visibilidad del texto y la url con las tarifas
				this.tarifas();
				this.pageOrigen.terminarIteracion();
			}

		} else if (aprobarTx.equals("Cancelar")) {
			this.click(btnCancelarTx);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se cancelo la transacción.");
			// Permite validar la visibilidad del texto y la url con las tarifas
			this.tarifas();
			Reporter.reportEvent(Reporter.MIC_PASS,
					"Se cancelo la transacción, pero se realizara la consulta de los servicios.");
			this.consultarAdminUsuarios();
		}

		return results;
	}

	// Retornara los resultados los cuales seran llevados a middle
	public List<HashMap<String, String>> getResults() {
		return results;
	}

	// Si la fecha no fue nula, esta se llevara a middle para hacer las consultas
	public String getTxDate() {
		return txDate;
	}

	public void autorizarServicios(List<HashMap<String, String>> data) throws Exception {
		// Verificar si resultadoContratarEliminarServicios es nulo Obtener la lista de
		// servicios procesados Verificar si la lista no es nula Recorrer la lista e
		// imprimir información
		int contarContratar = 0;
		String aprobarTx = SettingsRun.getTestData().getParameter("Aprobar Tx").trim();
		String permisos = SettingsRun.getTestData().getParameter("Permisos").trim(); //
		String empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		String autorizar = SettingsRun.getTestData().getParameter("Autorizar servicios").trim();

		for (HashMap<String, String> op : data) {
			String tipoPrueba = op.get("tipoPruebaAprobada");
			if ("Contratar".equals(tipoPrueba)) {
				contarContratar++;
			}
		}

		if (contarContratar != 0 && aprobarTx.equals("Aprobar")) {
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se encontró la siguiente cantidad de servicios contratados para autorizar: " + contarContratar);

			if (autorizar.equals("Si")) {
				this.consultarAdminUsuarios();

				for (HashMap<String, String> contratado : data) {
					String valorLimite = SettingsRun.getTestData().getParameter("Valor de Limite").trim();
					String limiteTx = SettingsRun.getTestData().getParameter("Limite Transaccional").trim();
					String tipoPrueba = contratado.get("tipoPruebaAprobada");
					String servicioAprobado = contratado.get("servicioAprobado");
					DXCUtil.wait(3);
					if (tipoPrueba != null && tipoPrueba.equals("Contratar")) {

						// Xpath que verifica si el servicio se encuentra en la tabla
						// SERVICIOS se reemplaza con el servicio aprobado
						By xpathServicios = By.xpath("//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'"
								+ servicioAprobado + "')]");
						WebElement servicioLista = null;

						// Xpath que permite dar click en el input de acuerdo al tipo de servicio
						// SERVICIOS se reemplaza con el servicio aprobado para validar que el checkbox
						// coincide
						String xpathInputServicio = xpathAutorizarServicio.replace("SERVICIOS", servicioAprobado);
						By xpathSelectServicio = By.xpath(xpathInputServicio);
						WebElement checkboxServicio = null;
						// Verifica si el servicio se encuentra seleccionado
						try {
							servicioLista = this.findElement(xpathServicios);
							DXCUtil.wait(2);
							checkboxServicio = this.findElement(xpathSelectServicio);
						} catch (Exception e) {
							Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el servicio: " + servicioAprobado);
						}
						try {
							boolean seleccionado = checkboxServicio.isSelected();
							DXCUtil.wait(2);
							if (servicioLista != null && checkboxServicio.isDisplayed() && checkboxServicio.isEnabled()
									&& !seleccionado) {

								this.focus(checkboxServicio);
								checkboxServicio.click();
								Reporter.reportEvent(Reporter.MIC_PASS,
										"Se ha seleccionado el servicio: " + servicioAprobado);
								Evidence.save("Evidencia selección de servicio");
								DXCUtil.wait(2);
								if (servicioAprobado.equals("Apertura de Productos Digitales")
										&& servicioAprobado.equals("Consultas de Productos")
										&& servicioAprobado.equals("Inscripciones")
										&& servicioAprobado.equals("Transferencia Paypal")) {
									Reporter.reportEvent(Reporter.MIC_INFO,
											"No cuenta con limites transaccionales el servicio: " + servicioAprobado);
								} else {

									WebElement elementoLimiteTx = null;
									WebElement elementoValorLimiteTx = null;

									try {
										elementoLimiteTx = this.findElement(
												By.xpath("//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'"
														+ servicioAprobado
														+ "')]/../td/table//td/table//label[contains(text(),'"
														+ limiteTx + "')]/../input[@type='radio']"));

									} catch (Exception e) {
										try {
											elementoLimiteTx = this.findElement(By.xpath(
													"//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'"
															+ servicioAprobado
															+ "')]/../td/table//td/table//span/label[contains(text(),'"
															+ limiteTx + "')]/../input[@type='radio']"));
										} catch (Exception ex) {
											Reporter.reportEvent(Reporter.MIC_FAIL,
													"No coincide ninguno de los elemento.");
										}
									}

									if (elementoLimiteTx != null) {
										// Realizar acciones adicionales con el elemento encontrado
										this.focus(elementoLimiteTx);
										elementoLimiteTx.click();
										DXCUtil.wait(2);
										Evidence.save("Evidencia selección de limite");
										try {
											elementoValorLimiteTx = this.findElement(By.xpath(
													"//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'"
															+ servicioAprobado
															+ "')]/../td/table//td/table//label[contains(text(),'"
															+ limiteTx
															+ "')]/../input[@type='radio']/../../../../../../td/input[@type='text']"));
											Reporter.reportEvent(Reporter.MIC_INFO,
													"Se encontró el input sin el span.");
										} catch (Exception e) {
											try {
												elementoValorLimiteTx = this.findElement(By.xpath(
														"//*[@id='cphCuerpo_gvServicio']/tbody/tr/td[contains(text(),'"
																+ servicioAprobado
																+ "')]/../td/table//td/table//span/label[contains(text(),'"
																+ limiteTx
																+ "')]/../input[@type='radio']/../../../../../../../td/input[@type='text']"));
												Reporter.reportEvent(Reporter.MIC_INFO,
														"Se encontró el input con el span.");
											} catch (Exception ex) {
												Reporter.reportEvent(Reporter.MIC_FAIL,
														"No se encontró locator para esta transacción: "
																+ ex.getMessage());
											}
										}
										if (elementoValorLimiteTx != null && limiteTx.equals("Limitado")) {
											DXCUtil.wait(5);
											this.focus(elementoValorLimiteTx);
											String ValorLimitado = elementoValorLimiteTx.getText();

											if (valorLimite != ValorLimitado) {
												elementoValorLimiteTx.clear();
												elementoValorLimiteTx.sendKeys(valorLimite);
											} else {
												elementoValorLimiteTx.clear();
												elementoValorLimiteTx.sendKeys(valorLimite);
											}
											Evidence.save("Evidencia ingreso limite");

											double value = Double.parseDouble(valorLimite);
											Locale locale = new Locale("es", "CO");
											NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
											String formatedNumber = numberFormat.format(value);

											Reporter.reportEvent(Reporter.MIC_PASS,
													"Se selecciono " + limiteTx
															+ ", se implemento correctamente el valor limite: "
															+ formatedNumber);
										} else if (elementoValorLimiteTx != null && limiteTx.equals("Ilimitado")) {
											Reporter.reportEvent(Reporter.MIC_PASS,
													"Se selecciono " + limiteTx + ", no se ingresa valor limite.");
											Evidence.save("Evidencia no se ingresa valor limite");
										}
									}
								}
							} else {
								// Acceder a la ruta
								this.pageOrigen.irAOpcion("", "Administración", "Administración Portal",
										"Administracion de Usuarios");
								WebElement seleccEmpresa = this.findElement(selectEmpresa);
								this.selectListItem(seleccEmpresa, empresa);
								Evidence.saveFullPage("Evidencia Administracion de usuarios", this);
							}

						} catch (Exception e) {
							Reporter.reportEvent(Reporter.MIC_FAIL, e.getMessage());
						}

					}

				}

				DXCUtil.wait(2);
				WebElement seleccPermisos = this.findElement(selectPermisos);
				this.selectListItem(seleccPermisos, permisos);
				Evidence.saveFullPage("Evidencia selección de permisos", this);
				DXCUtil.wait(5);
				WebElement aprovPagos = this.findElement(aprovPagosTransfer);
				WebElement creacPagos = this.findElement(creacionPagosTransfer);
				WebElement programPagos = this.findElement(programRetiSinTarj);
				DXCUtil.wait(1);
				this.focus(programPagos);
				// Cada condición valida si el check esta seleccionado no lo seleccione, pero si
				Evidence.save("Evidencia selección de permisos");
				if (!aprovPagos.isSelected()) {
					aprovPagos.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó Aprobación de pagos y transferencias");
					Evidence.save("Evidencia seleccion");
				} else {
					Reporter.reportEvent(Reporter.MIC_INFO,
							"Ya se encuentra seleccionado Aprobación de pagos y transferencias");
					Evidence.save("Evidencia ya seleccionado");
				}

				if (!creacPagos.isSelected()) {
					creacPagos.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó Creación de pagos y transferencias");
					Evidence.save("Evidencia seleccion");
				} else {
					Reporter.reportEvent(Reporter.MIC_INFO,
							"Ya se encuentra seleccionado Creación de pagos y transferencias");
					Evidence.save("Evidencia ya seleccionado");
				}

				if (!programPagos.isSelected()) {
					programPagos.click();
					Reporter.reportEvent(Reporter.MIC_PASS, "Se seleccionó Programación retiros sin tarjeta");
					Evidence.save("Evidencia seleccion");
				} else {
					Reporter.reportEvent(Reporter.MIC_INFO,
							"Ya se encuentra seleccionado Programación retiros sin tarjeta");
					Evidence.save("Evidencia ya seleccionado");
				}

				this.focus(btnGuardar);
				Evidence.save("Evidencia selección de permisos");
				DXCUtil.wait(2);
				this.aprobarTransaccion();
				Reporter.reportEvent(Reporter.MIC_PASS, "Guardados los cambios correctamente.");
				DXCUtil.wait(2);

			} else if (autorizar.equals("No")) {
				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se sacara evidencia de los servicios que se encuentran actualmente, no se autorizara ninguno.");
				Evidence.saveFullPage("Evidencia No autorización", this);
				this.consultarAdminUsuarios();
			}
		} else if (aprobarTx.equals("Cancelar")) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"Se cancelo la transacción, pero se realizara la consulta de los servicios.");
			Evidence.saveFullPage("Evidencia se cancela el servicio", this);
			this.consultarAdminUsuarios();
		}
	}

	public void consultarAdminUsuarios() throws Exception {
		// Acceder a la ruta
		this.pageOrigen.irAOpcion("", "Administración", "Administración Portal", "Administracion de Usuarios");
		Evidence.saveFullPage("Evidencia Administracion de usuarios", this);

		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se ingreso a la ruta: Administración/ Administración Portal Administracion de Usuarios");

		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		String usuarios = "//*[@id='cphCuerpo_gvUsuarios']/tbody/tr/td[contains(text(),'" + numeroDocumento + "')]";
		String btnEditar = "//*[@id='cphCuerpo_gvUsuarios']/tbody/tr/td[contains(text(),'" + numeroDocumento
				+ "')]/../td/a[contains(text(),'Editar')]";

		Evidence.save("Evidencia Administracion de usuarios");
		DXCUtil.wait(2);
		List<WebElement> tableusers = this.findElements(tablaAdmiUsuarios);
		for (WebElement elemento : tableusers) {
			WebElement usuario = this.findElement(By.xpath(usuarios));
			if (usuario.isDisplayed()) {
				this.findElement(By.xpath(btnEditar)).click();
				DXCUtil.wait(5);
				Evidence.saveFullPage("Evidencia Administracion de usuarios", this);
			} else {
				WebElement btnSiguiente = this.findElement(btnSiguienteRg);
				btnSiguiente.click();
				Evidence.save("Evidencia Administracion de usuarios");
			}
		}
		Reporter.reportEvent(Reporter.MIC_PASS, "Se encontró el registro");

		DXCUtil.wait(10);
		WebElement seleccEmpresa = this.findElement(selectEmpresa);
		this.selectListItem(seleccEmpresa, empresa);
		DXCUtil.wait(3);
		Reporter.reportEvent(Reporter.MIC_PASS, "Se Selecciono la empresa correctamente");
		Evidence.saveFullPage("Evidencia Administracion de usuarios", this);

	}

	// Metódo para acceder a la ruta de front
	public void accesoRutaFront() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		String msgError = null;

		String pagoyTx = this.pageOrigen.getTextMid();
		if (pagoyTx.contains("Administración")) {
			DXCUtil.wait(1);
			msgError = pageOrigen.irAOpcion("", "Administración", "Administración", "Adquiera productos y Servicios",
					"Servicios del portal");
		} else if (pagoyTx.contains("Administración")) {
			DXCUtil.wait(1);
			msgError = pageOrigen.irAOpcion("", "Administración", "Administración portal",
					"Adquiera productos y servicios", "Servicios del Portal");

		} else {
			DXCUtil.wait(1);
			msgError = pageOrigen.irAOpcion("", "Administración", "Administración Portal",
					"Adquiera productos y Servicios", "Servicios del Portal");
		}
		if (msgError != null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
			this.pageOrigen.terminarIteracion();
		}

		Evidence.saveFullPage("Autoservicio", this);
	}

	// Método que permite ver las tarifas
	public void tarifas() throws Exception {

		WebElement textTarifas = this
				.findElement(By.xpath("//*[@id='cphCuerpo_pnlContenido']/table[2]/tbody/tr/td/span[2]/p/b"));
		String tarifas = textTarifas.getText();
		WebElement linkAqui = this
				.findElement(By.xpath("//*[@id='cphCuerpo_pnlContenido']/table[2]/tbody/tr/td/span[2]/p/b/a"));
		String urlTarifas = linkAqui.getAttribute("href");

		if (tarifas != null && linkAqui != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Se visualizo el mensaje de tarifas: " + tarifas
					+ "\n puede visualizarlas por medio del siguiente link: " + urlTarifas);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro el siguiente contenido: \n - link tarifas: "
					+ urlTarifas + "\n - Mensaje: " + tarifas);
		}
	}

	// Método que aprobara las TX y se pueda guardar la acción realizada,
	// permitiendo la OTP, token, etc.
	public String aprobarTransaccion() throws Exception {
		this.pageOrigen = new PageOrigen(this);

		this.pageConfirmacion = new PageConfirmacion(this);
		this.pageConfirmacion.aprobarTx();
		DXCUtil.wait(2);

		this.pagePortalPymes = new PagePortalPymes(this);
		String mensaje = this.pagePortalPymes.getMsgAlertIfExist("lblMasterAlerta");
		if (mensaje != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Se visualizo el siguiente mensaje informativo: " + mensaje);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut al aprobar la transacción.");
		}

		Evidence.saveAllScreens("ConfirmarContr", this);
		return mensaje;
	}

}
