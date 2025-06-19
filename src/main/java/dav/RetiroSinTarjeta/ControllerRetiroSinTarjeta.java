package dav.RetiroSinTarjeta;

import dav.Recaudos.PageRecaudos;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.library.reporting.Reporter;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;

public class ControllerRetiroSinTarjeta {

	PageRetiroSinTarjeta pageRetiroSinTarjeta = null;
	PageLoginPymes pageLogPymes = null;
	PageOrigen pageOrigen = null;
	int eventStatus = Reporter.MIC_PASS;
	static String mensajeReport = null;

	public ControllerRetiroSinTarjeta(PageLoginPymes pageParent) {
		pageLogPymes = pageParent;
		pageOrigen = new PageOrigen(pageParent);
	}

	// Capturados de Mensajes y reportes del escenario
	public void PendienteRetiro() throws Exception {
		this.pageRetiroSinTarjeta = new PageRetiroSinTarjeta(pageLogPymes);
		String msgerror = this.pageOrigen.irAOpcion(null, "Pagos, Transferencias y Otros", "Pendientes de Aprobación");
		if (msgerror != null)
			this.pageOrigen.terminarIteracion(eventStatus, msgerror);
		Reporter.reportEvent(eventStatus, "Redireccion al modulo exitosa,segun lo esperado");
		String arrresp[] = this.pageRetiroSinTarjeta.PendienteRetiro();
		if (arrresp.length > 1) {
			for (int i = 0; i < arrresp.length; i++) {
				if (!arrresp[i].contains("segun lo esperado")) {
					eventStatus = Reporter.MIC_FAIL;
					this.pageOrigen.terminarIteracion(eventStatus, arrresp[i]);
				}
				Reporter.reportEvent(eventStatus, arrresp[i]);
			}
		} else {
			eventStatus = Reporter.MIC_FAIL;
			this.pageOrigen.terminarIteracion(eventStatus, arrresp[0]);

		}
	}

}
