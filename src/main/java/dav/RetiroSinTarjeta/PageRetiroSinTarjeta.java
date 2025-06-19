package dav.RetiroSinTarjeta;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.pymes.moduloTx.PagePendAprobacion;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageRetiroSinTarjeta extends BasePageWeb {

	// Locators
	By consultaMov = By.xpath("//*[@id='lblMasterAlerta']/a[1]");
	String NumDocumento = "//table//span[text()='NumDocumento']";
	String Detalle ="//table//a[text()='NumDocumento']";
	WebElement movimientoReciente = null;
	By detalleTitulo = By.xpath("//*[@id='lblMasterTitulo']");
	By eliminarRetiro = By.xpath("//*[@id='cphCuerpo_btnEliminar']");
	By Token = By.xpath("//*[@id='cphCuerpo_tbxTokenOtp']");
	By confEliminacion = By.xpath("//*[@id='cphCuerpo_btnConfirmarTransaccion']");
	By Alerta = By.xpath("//*[@id='lblMasterAlerta']");
	By eliminarRetiroDetalle = By.xpath("//*[@id='cphCuerpo_btnEliminartrx']");
	By tokenDetalle = By.xpath("//*[@id='cphCuerpo_tbxTokenOtpApEl']");
	By confEliminarDetalle = By.xpath("//*[@id='cphCuerpo_btnConfirmarTransaccionApEl']");
	By alertauno = By.xpath("//*[@id='panMasterAlerta']/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]");

	/*
	 * Constructor y Pages
	 */
	PageLoginPymes pageLoginPymes = null;
	PageOrigen pageOrigen = null;
	PagePendAprobacion pagePendAprobacion = null;
	PageConfirmacion pageConfirmacion = null;

	String msgerror[] = null;

	public PageRetiroSinTarjeta(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
		this.pageLoginPymes = new PageLoginPymes(parentPage);
		this.pageOrigen = new PageOrigen(parentPage);
		this.pageConfirmacion = new PageConfirmacion(parentPage);
	}

	public String[] PendienteRetiro() throws Exception {
		String NumRetiro = SettingsRun.getTestData().getParameter("Numero de Proceso del Retiro").trim();
		String TipoP = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		String numIntentos = SettingsRun.getTestData().getParameter("Intentos Fallidos").trim();
		String siNoDetalle = SettingsRun.getTestData().getParameter("Dentro del detalle").trim();
		String token = SettingsRun.getTestData().getParameter("Semilla / Valor Estático / Celular").trim();
		int retofallidos =Integer.valueOf(numIntentos);
//		Evidence.save("Pantalla Pendientes", this);
		Evidence.save("Pantalla Pendientes");
		DXCUtil.wait(1);
		if (TipoP.contains("Aprobar")) {
			if (siNoDetalle.equals("NO")) {
				msgerror = new String[3];
				this.pagePendAprobacion = new PagePendAprobacion(pageLoginPymes);
				String Validacion = this.pagePendAprobacion.seleccionarTransaccion(NumRetiro);
				if (Validacion != null)
					return new String[] { Validacion };
				else
					msgerror[0] = "Se seleciono el Proceso Indicado, segun lo esperado";

				String Vallidacion1 = this.pageConfirmacion.aprobarTx(retofallidos, false);
				if (!Vallidacion1.contains("Sus transacciones han sido recibidas por el Portal PYMES"))
					return new String[] { Vallidacion1 };
				else
					msgerror[1] = "Su transaccion ha sido recibida por el Portal PYMES, segun lo esperado";

				this.click(consultaMov);
				DXCUtil.wait(1);
//				Evidence.save("Modulo de consulta del movimien " + NumRetiro, this);
				Evidence.save("Modulo de consulta del movimien " + NumRetiro);
				String xpathRemplazarNumDocumento = NumDocumento.replace("NumDocumento", NumRetiro);
				movimientoReciente = this.element(By.xpath(xpathRemplazarNumDocumento));
				if (movimientoReciente != null) {
					msgerror[2] = "Su transaccion " + NumRetiro + " ha sido Encontrada en las Transacciones Recientes del Portal PYMES, segun lo esperado";
				} else {
					return new String[] {
							"Su transaccion No ha sido encontrada en las Transacciones Recientes del Portal" };
				}

			}
			if (siNoDetalle.equals("SI")) {
				msgerror = new String[5];
				this.pagePendAprobacion = new PagePendAprobacion(pageLoginPymes);
				String Validacion = this.pagePendAprobacion.seleccionarTransaccion(NumRetiro);
				if (Validacion != null)
					return new String[] { Validacion };
				else
					msgerror[0] = "Se seleciono el Proceso Indicado, segun lo esperado";
				
				String xpathRemplazarNumRetiroDetalle = Detalle.replace("NumDocumento", NumRetiro);
				WebElement detalleRetiro = this.element(By.xpath(xpathRemplazarNumRetiroDetalle));
				if (detalleRetiro != null) {
					msgerror[1] = "Su transaccion " + NumRetiro + " ha sido Encontrada en las Transacciones pendientes del Portal PYMES Con el detalle, segun lo esperado";
				} else {
					return new String[] {"Su transaccion No ha sido encontrada en las Transacciones Recientes del Portal" };
				}
				this.click(detalleRetiro);
				DXCUtil.wait(1);
//				Evidence.save("Detalle del movimiento",this);
				Evidence.save("Detalle del movimiento");
				String titulo = this.element(detalleTitulo).getText();
				if(titulo.contains("Detalle de Transacción")) {
					msgerror[2] = "Dentro del Detalle de la Transaccion " + NumRetiro +", segun lo esperado";
				}else {
					return new String[] {"No se ha podido ingresar al detalle de su Transaccion" };
				}
				
				String Vallidacion1 = this.pageConfirmacion.aprobarTx(retofallidos, false);
				if (!Vallidacion1.contains("Su transacci�n ha sido recibida por el Portal PYME"))
					return new String[] { Vallidacion1 };
				else
					msgerror[3] = "Su transaccion ha sido recibida por el Portal PYMES, segun lo esperado";

				this.click(consultaMov);
				DXCUtil.wait(1);
//				Evidence.save("Modulo de consulta del movimien " + NumRetiro, this);
				Evidence.save("Modulo de consulta del movimien " + NumRetiro);
				String xpathRemplazarNumDocumento = NumDocumento.replace("NumDocumento", NumRetiro);
				movimientoReciente = this.element(By.xpath(xpathRemplazarNumDocumento));
				if (movimientoReciente != null) {
					msgerror[4] = "Su transaccion " + NumRetiro + " ha sido Encontrada en las Transacciones Recientes del Portal PYMES, segun lo esperado";
				} else {
					return new String[] {
							"Su transaccion No ha sido encontrada en las Transacciones Recientes del Portal" };
				}
				
				
			}
			if (siNoDetalle == null || siNoDetalle.equals("")) {
				return new String[] { "Tipo de pruba del Detalle vacio o invalido" };

			}

		}
		if (TipoP.contains("Eliminar")) {
			if(siNoDetalle.equals("NO")) {
			msgerror = new String[3];
			this.pagePendAprobacion = new PagePendAprobacion(pageLoginPymes);
			String Validacion = this.pagePendAprobacion.seleccionarTransaccion(NumRetiro);
			if (Validacion != null)
				return new String[] { Validacion };
			else
				msgerror[0] = "Se seleciono el Proceso Indicado, segun lo esperado";
		
			DXCUtil.wait(1);
			this.focus(eliminarRetiro);
			this.click(eliminarRetiro);
			Evidence.save("Mensaje de Alerta de Eliminacion del Proceso "+NumRetiro);
			this.acceptDialog();
			Evidence.save("Proceso Confirmacion");
			DXCUtil.wait(2);
			this.focus(Token);
			this.write(Token,token);
			DXCUtil.wait(2);
			Evidence.save("Ingreso Token");
			DXCUtil.wait(1);
			this.mouseOver(eliminarRetiro);
			this.mouseClick();
			this.click(confEliminacion);
			DXCUtil.wait(5);
			WebElement alert = this.findElement(alertauno);
			if(alert!=null) {
			this.focus(Alerta);
			String msgAlert = this.getText(Alerta);
			if(msgAlert.contains("Usted elimin� 1 Transacci�n")) {
				msgerror[1] =msgAlert+", segun lo esperado";
			}else {
				return new String[] {msgAlert};
			}
			}else {
				return new String[] {"Mal cargue,intentelo nuevamente con otro retiro"};
			}
			String xpathRemplazarNumRetiroDetalle = Detalle.replace("NumDocumento", NumRetiro);
			WebElement detalleRetiro = this.element(By.xpath(xpathRemplazarNumRetiroDetalle));
			if (detalleRetiro == null) {
				msgerror[2] = "Su transaccion " + NumRetiro + " ha sido Eliminada de las Transacciones pendientes del Portal PYMES , segun lo esperado";
			} else {
				return new String[] {"Su transaccion ha sido encontrada en las Transacciones Pendientes del Portal" };
			}
		
			Evidence.save("Eliminacion Exitosa");
			}
			
			if(siNoDetalle.equals("SI")){
				msgerror = new String[4];
				this.pagePendAprobacion = new PagePendAprobacion(pageLoginPymes);
				String Validacion = this.pagePendAprobacion.seleccionarTransaccion(NumRetiro);
				if (Validacion != null)
					return new String[] { Validacion };
				else
					msgerror[0] = "Se seleciono el Proceso Indicado, segun lo esperado";
				
				String xpathRemplazarNumRetiroDetalle = Detalle.replace("NumDocumento", NumRetiro);
				WebElement detalleRetiro = this.element(By.xpath(xpathRemplazarNumRetiroDetalle));
				if (detalleRetiro != null) {
					msgerror[1] = "Su transaccion " + NumRetiro + " ha sido Encontrada en las Transacciones pendientes del Portal PYMES Con el detalle, segun lo esperado";
				} else {
					return new String[] {"Su transaccion No ha sido encontrada en las Transacciones Recientes del Portal" };
				}
				this.click(detalleRetiro);
				DXCUtil.wait(1);
//				Evidence.save("Detalle del movimiento",this);
				Evidence.save("Detalle del movimiento");
				String titulo = this.element(detalleTitulo).getText();
				if(titulo.contains("Detalle de Transacción")) {
					msgerror[2] = "Dentro del Detalle de la Transaccion " + NumRetiro +", segun lo esperado";
				}else {
					return new String[] {"No se ha podido ingresar al detalle de su Transaccion" };
				}
				DXCUtil.wait(1);
				this.focus(eliminarRetiroDetalle);
				this.click(eliminarRetiroDetalle);
				Evidence.save("Mensaje de Alerta de Eliminacion del Proceso "+NumRetiro);
				this.acceptDialog();
				Evidence.save("Proceso Confirmacion de Eliminacion");
				DXCUtil.wait(2);
				this.focus(tokenDetalle);
				this.write(tokenDetalle,token);
				DXCUtil.wait(2);
				Evidence.save("Ingreso Token");
				DXCUtil.wait(1);
				this.mouseOver(eliminarRetiroDetalle);
				this.mouseClick();
				this.click(confEliminarDetalle);
				DXCUtil.wait(3);
				
				String xpathRemplazarNumRetiroDetalleEliminacion = Detalle.replace("NumDocumento", NumRetiro);
				WebElement detalleRetiro1 = this.element(By.xpath(xpathRemplazarNumRetiroDetalleEliminacion));
				if (detalleRetiro1 == null) {
					msgerror[3] = "Su transaccion " + NumRetiro + " ha sido Eliminada de las Transacciones pendientes del Portal PYMES, segun lo esperado";
				} else {
					return new String[] {"Su transaccion ha sido encontrada en las Transacciones Pendientes del Portal" };
				}
			
				Evidence.save("Eliminacion Exitosa");
				
				
				
			}
			if (siNoDetalle == null || siNoDetalle.equals("")) {
				return new String[] { "Tipo de pruba del Detalle vacio o invalido" };

			}
		}

		if (TipoP == null || TipoP.equals("")) {
			return new String[] { "Tipo de prueba vacio o invalido" };

		}

		return msgerror;
	}

}
