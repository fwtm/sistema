/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function limitText(limitNum) {
    limitField = document.getElementById('form_cont:contTab:observacion').value;
    limitCount = document.getElementById('form_cont:contTab:countdown').value;
    if (limitField.length > limitNum) {
        document.getElementById('form_cont:contTab:observacion').value = limitField.substring(0, limitNum);
    } else {
        document.getElementById('form_cont:contTab:countdown').value = limitField.length;
    }
}

function cerrarMsjD() {
    var msj = document.getElementById('form_cont:contTab:mssgsBusquedaErrorM');
    msj.style.display = "none";
}

/**********************/
function valNumEmbarazos() {
    var ne = document.getElementById("form_cont:contTab:numeroEmbarazosMad").value;
    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    var msg = document.getElementById("form_cont:contTab:numeroEmbarazosMadMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgNumEmbarazos");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(ne) >= parseInt(np)) {
        if (!(ne >= 1 && ne <= 38)) {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgNumEmbarazos");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de embarazos no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
        return "T";
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgNumEmbarazos");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de embarazos debe ser mayor o igual al número de parto del sistema.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
}

function valContPrenatales() {
    var cp = document.getElementById("form_cont:contTab:cntrlPrntlNacViv").value;
    var msg = document.getElementById("form_cont:contTab:cntrlPrntlNacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgContPrenatales");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (cp > 25) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgContPrenatales");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de controles prenatales no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosVivSistema() {
    var hv = document.getElementById("form_cont:contTab:hijosNacierVivSistema").value;
    var msg = document.getElementById("form_cont:contTab:hijosNacierVivSistemaMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosVivSistema");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(hv >= 1 && hv <= 10)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgHijosVivSistema");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de hijos que nacieron vivos en este parto no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosMuertSistema() {
    var hm = document.getElementById("form_cont:contTab:hijosNacierMuertSistema").value;
    var msg = document.getElementById("form_cont:contTab:hijosNacierMuertSistemaMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosMuertSistema");
    //
    var hv = document.getElementById("form_cont:contTab:hijosNacierVivSistema").value;
    var element = document.getElementById("form_cont:contTab:fkIdProEmb_input");
    var pe = element.options[element.selectedIndex].value;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (hv !== "" && pe !== "" && hm !== "") {
        if (!(hm >= 0 && hm <= 9)) {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuertSistema");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos que nacieron muertos en este parto no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        } else {
            if ((parseInt(hv) + parseInt(hm)) !== parseInt(pe)) {
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuertSistema");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "La suma de hijos nacidos vivos y nacidos muertos correspondientes a este parto debe ser igual al producto del embarazo.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        }
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuertSistema");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "Primero debe ingresar los valores de producto del embrazo, número de hijos vivos y número de hijos muertos de este parto.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

/*Para la validación de los datos historicos*/
function valNumeroParto() {
    var nps = document.getElementById("form_cont:contTab:numPartoSistema").textContent;
    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    var msg = document.getElementById("form_cont:contTab:numerPartoNacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgNumeroParto");
    var edad = document.getElementById("form_cont:contTab:edadMad").textContent;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(np) >= parseInt(nps)) {
        if (!(np >= 1 && np <= 19) || !(parseInt(np) <= ((parseInt(edad) - 7) * 2))) {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgNumeroParto");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de partos no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
        document.getElementById("form_cont:contTab:hijosVivsaMad").disabled = false;
        document.getElementById("form_cont:contTab:hijosNmrtsMad").disabled = false;
        //document.getElementById("form_cont:contTab:hijosVivsaMad").focus();
        return "T";
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgNumeroParto");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de partos históricos debe ser mayor o igual al número del parto actual.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        //encero los datos nuevamente
        document.getElementById("form_cont:contTab:hijosVivsaMad").value = "1";
        document.getElementById("form_cont:contTab:hijosNmrtsMad").value = "0";
        //Desabilito las cajas de texto de los datos demas datos historicos
        document.getElementById("form_cont:contTab:hijosVivsaMad").disabled = true;
        document.getElementById("form_cont:contTab:hijosNmrtsMad").disabled = true;
        //document.getElementById("form_cont:contTab:numerPartoNacViv").focus();
        return "F";
    }
}

function valPartoUno() {
    var nps = document.getElementById("form_cont:contTab:numPartoSistema").textContent;
    var hvs = document.getElementById("form_cont:contTab:hijosNacierVivSistema").value;
    var hms = document.getElementById("form_cont:contTab:hijosNacierMuertSistema").value;

    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    //Hijos vivos histórico
    var msgHv = document.getElementById("form_cont:contTab:hijosVivsaMadMsg");
    var txtMsgHv = document.getElementById("form_cont:contTab:txtMsgHijosViv");
    var hv = document.getElementById("form_cont:contTab:hijosVivsaMad").value;
    //Hijos vivos han muerto histórico
    var msgHvhm = document.getElementById("form_cont:contTab:hijosNvmrtMadMsg");
    var txtMsgHvhm = document.getElementById("form_cont:contTab:txtMsgHijosVivHanMuert");
    var hvhm = document.getElementById("form_cont:contTab:hijosNvmrtMad").value;
    //Hijos muertos histórico
    var msgHm = document.getElementById("form_cont:contTab:hijosNmrtsMadMsg");
    var txtMsgHm = document.getElementById("form_cont:contTab:txtMsgHijosMuert");
    var hm = document.getElementById("form_cont:contTab:hijosNmrtsMad").value;
    //Elimino los mensajes de error
    if (txtMsgHv !== null) {
        msgHv.removeChild(txtMsgHv);
    }
    if (txtMsgHvhm !== null) {
        msgHvhm.removeChild(txtMsgHvhm);
    }
    if (txtMsgHm !== null) {
        msgHm.removeChild(txtMsgHm);
    }
    var comprobacion1 = "T";
    var comprobacion2 = "T";
    var comprobacion3 = "T";
    if (parseInt(np) === parseInt("1")
            && parseInt(nps) === parseInt("1")) {

        if (parseInt(hvs) === parseInt(hv)) {
            comprobacion1 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos históricos debe ser igual al número de hijos vivos del parto actual.";
            msgHv.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHv.appendChild(span);
            comprobacion1 = "F";
        }
        if (parseInt(hms) === parseInt(hm)) {
            comprobacion2 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos muertos históricos debe ser igual al número de hijos muertos del parto actual.";
            msgHm.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHm.appendChild(span);
            comprobacion2 = "F";
        }
        if (parseInt(hvhm) === parseInt("0")) {
            comprobacion3 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosVivHanMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos que han muerto debe ser igual a cero.";
            msgHvhm.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHvhm.appendChild(span);
            comprobacion3 = "F";
        }
        //Comprobación Final
        if (comprobacion1 === "T" && comprobacion2 === "T"
                && comprobacion3 === "T") {
            return "T";
        } else {
            return "F";
        }
    }
    return "T";
}

function valHijosViv() {
    var nps = document.getElementById("form_cont:contTab:numPartoSistema").textContent;
    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    var hvs = document.getElementById("form_cont:contTab:hijosNacierVivSistema").value;
    var edad = document.getElementById("form_cont:contTab:edadMad").textContent;
    //
    var hv = document.getElementById("form_cont:contTab:hijosVivsaMad").value;
    var msg = document.getElementById("form_cont:contTab:hijosVivsaMadMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosViv");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    //Valido el numero de parto
    if (parseInt(np) >= parseInt(nps)) {
        if (parseInt(hv) >= parseInt(hvs)) {
            if (!(hv >= 1 && hv <= 19) || !(parseInt(hv) <= ((parseInt(edad) - 7) * 4))) {
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos históricos debe ser mayor o igual al número actual de hijos vivos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else {
        if (!(hv >= 1 && hv <= 19) || !(parseInt(hv) <= ((parseInt(edad) - 7) * 4))) {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}


function valHijosViv1() {
    var nps = document.getElementById("form_cont:contTab:numPartoSistema").textContent;
    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    var hvs = document.getElementById("form_cont:contTab:hijosNacierVivSistema").value;
    var edad = document.getElementById("form_cont:contTab:edadMad").textContent;
    //
    var hv = document.getElementById("form_cont:contTab:hijosVivsaMad").value;
    var msg = document.getElementById("form_cont:contTab:hijosVivsaMadMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosViv");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
//INICIO ---0001092 DFJ ****Validar num hijos con partos historicos
    try {
        var hhv = document.getElementById("form_cont:contTab:OlblhijosVivsaMad").textContent;
        if (hhv !== null) {
            hvs = parseInt(hvs) + parseInt(hhv);
        }
    }
    catch (err) {
    }
    //FIN DFJ

    //Valido el numero de parto
    if (parseInt(np) >= parseInt(nps)) {
        if (parseInt(hv) >= parseInt(hvs)) {
            if (!(hv >= 1 && hv <= 19) || !(parseInt(hv) <= ((parseInt(edad) - 7) * 4))) {
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos históricos debe ser mayor o igual al número actual de hijos vivos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else {
        if (!(hv >= 1 && hv <= 19) || !(parseInt(hv) <= ((parseInt(edad) - 7) * 4))) {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}



function valHijosVivHanMuert() {
    var hv = document.getElementById("form_cont:contTab:hijosNvmrtMad").value;
    var msg = document.getElementById("form_cont:contTab:hijosNvmrtMadMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosVivHanMuert");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(hv >= 0 && hv <= 18)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgHijosVivHanMuert");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de hijos que nacieron vivos y han muerto no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosMuert() {
    var nps = document.getElementById("form_cont:contTab:numPartoSistema").textContent;
    var np = document.getElementById("form_cont:contTab:numerPartoNacViv").value;
    var hms = document.getElementById("form_cont:contTab:hijosNacierMuertSistema").value;
    //
    var hm = document.getElementById("form_cont:contTab:hijosNmrtsMad").value;
    var msg = document.getElementById("form_cont:contTab:hijosNmrtsMadMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgHijosMuert");
    //
    var hv = document.getElementById("form_cont:contTab:hijosVivsaMad").value;
    var hvhm = document.getElementById("form_cont:contTab:hijosNvmrtMad").value;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(np) >= parseInt(nps)) {
        if (parseInt(hm) >= parseInt(hms)) {
            if (np !== "" && hv !== "" && hvhm !== "" && hm !== "") {
                if (!(hm >= 0 && hm <= 18)) {
                    var span = document.createElement('span');
                    span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
                    span.setAttribute("class", "ui-message-error-detail");
                    span.textContent = "El número de hijos que nacieron muertos no es válido.";
                    msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                    msg.appendChild(span);
                    return "F";
                } else {
                    if (parseInt(np) > (parseInt(hv) + parseInt(hvhm) + parseInt(hm))) {
                        var span = document.createElement('span');
                        span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
                        span.setAttribute("class", "ui-message-error-detail");
                        span.textContent = "La suma de hijos vivos, hijos vivos que han muerto e hijos que nacieron muertos debe ser mayor o igual al número del parto.";
                        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                        msg.appendChild(span);
                        return "F";
                    }
                }
            } else {
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "Primero debe ingresar los valores de número de parto, hijos vivos que tiene actualmente, hijos vivos que han muerto e hijos que nacieron muertos.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos que nacieron muertos históricos debe ser mayor o igual al número actual de hijos que nacieron muertos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else {
        if (np !== "" && hv !== "" && hvhm !== "" && hm !== "") {
            if (!(hm >= 0 && hm <= 18)) {
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "El número de hijos que nacieron muertos no es válido.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            } else {
                if (parseInt(np) > (parseInt(hv) + parseInt(hvhm) + parseInt(hm))) {
                    var span = document.createElement('span');
                    span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
                    span.setAttribute("class", "ui-message-error-detail");
                    span.textContent = "La suma de hijos vivos, hijos vivos que han muerto e hijos que nacieron muertos debe ser mayor o igual al número del parto.";
                    msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                    msg.appendChild(span);
                    return "F";
                }
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "Primero debe ingresar los valores de número de parto, hijos vivos que tiene actualmente, hijos vivos que han muerto e hijos que nacieron muertos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}

function valSemGestacion() {
    var sg = document.getElementById("form_cont:contTab:semanGstcnNacViv").value;
    var msg = document.getElementById("form_cont:contTab:semanGstcnNacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgSemGestacion");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(sg >= 22 && sg <= 42)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgSemGestacion");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de semanas de gestación no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function saveM1() {
    var vcp = valContPrenatales();
    var vhvs = valHijosVivSistema();
    var vhms = valHijosMuertSistema();
    var vnp = valNumeroParto();
    var vhv = valHijosViv();
    var vhvhm = valHijosVivHanMuert();
    var vhm = valHijosMuert();
    var vsg = valSemGestacion();
    var vne = valNumEmbarazos();
    var vpu = valPartoUno();
    var vnm = valNomMadre();
    var vapm = valApellMadre();
    if (vcp === 'T'
            && vhvs === 'T'
            && vhms === 'T'
            && vnp === 'T'
            && vhv === 'T'
            && vhvhm === 'T'
            && vhm === 'T'
            && vsg === 'T'
            && vne === 'T'
            && vpu === 'T'
            && vnm === 'T'
            && vapm === 'T'
            ) {
//        if (!confirm('¿Está seguro que desea guardar el registro?')){
//            return false;  
//        }
        return true;
    } else {
        return false;
    }
}

function saveM() {
    var vcp = valContPrenatales();
    var vhvs = valHijosVivSistema();
    var vhms = valHijosMuertSistema();
    var vnp = valNumeroParto();
    var vhv = valHijosViv();
    var vhvhm = valHijosVivHanMuert();
    var vhm = valHijosMuert();
    var vsg = valSemGestacion();
    var vne = valNumEmbarazos();
    var vpu = valPartoUno();
    if (vcp === 'T' && vhvs === 'T' && vhms === 'T'
            && vnp === 'T' && vhv === 'T'
            && vhvhm === 'T' && vhm === 'T' && vsg === 'T' && vne === 'T'
            && vpu === 'T') {
//        if (!confirm('¿Está seguro que desea guardar el registro?')){
//            return false;  
//        }
        return true;
    } else {
        return false;
    }
}

function saveM2() {
    var vcp = valContPrenatales();//correcto
    var vhvs = valHijosVivSistema();//correcto
    var vhms = valHijosMuertSistema(); //correcto
    var vnp = valNumeroParto();//correcto
    var vhv = valHijosViv();//correcto
    var vhvhm = valHijosVivHanMuert();//correcto
    var vhm = valHijosMuert();//correcto
    var vsg = valSemGestacion();//correcto
    var vne = valNumEmbarazos();//correcto
    var vpu = valPartoUno();   //correcto
    var vnam = valNomMadre();
    var vnap = valApellMadre();
    if (vcp === 'T'
            && vhvs === 'T'
            && vhms === 'T'
            && vnp === 'T'
            && vhv === 'T'
            && vhvhm === 'T'
            && vhm === 'T'
            && vsg === 'T'
            && vne === 'T'
            && vpu === 'T'
            && vnam === 'T'
            && vnap === 'T'
            ) {
//        if (!confirm('¿Está seguro que desea guardar el registro?')){
//            return false;  
//        }
        return true;
    } else {
        return false;
    }
}

/*Validaciones para el hijo*/

function saveFechNacimAndSemGestac() {
    var vsg = valSemGestacion();
    if (vsg === 'T') {
        return true;
    } else {
        return false;
    }
}

function valTalla() {
    var t = document.getElementById("form_cont:contTab:tallaNacViv").value;
    var msg = document.getElementById("form_cont:contTab:tallaNacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgTalla");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(t >= 38 && t <= 52)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgTalla");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "La talla ingresada no es válida.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valPeso() {
    var t = document.getElementById("form_cont:contTab:pesoNacViv").value;
    var msg = document.getElementById("form_cont:contTab:pesoNacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgPeso");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(t >= 500 && t <= 5000)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgPeso");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El peso ingresado no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valApgar1() {
    var a = document.getElementById("form_cont:contTab:apgar1NacViv").value;
    var msg = document.getElementById("form_cont:contTab:apgar1NacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgApgar1");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(a >= 1 && a <= 10)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgApgar1");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El valor no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valApgar2() {
    var a = document.getElementById("form_cont:contTab:apgar2NacViv").value;
    var msg = document.getElementById("form_cont:contTab:apgar2NacVivMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgApgar2");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(a >= 1 && a <= 10)) {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgApgar2");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El valor no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function saveDatosNV() {
    var vt = valTalla();
    var vp = valPeso();
    var a1 = valApgar1();
    var a2 = valApgar2();
    var m = validarNumMalformaciones();
    if (vt === 'T' && vp === 'T' && a1 === 'T' && a2 === 'T' && m === 'T') {
//        if (!confirm('¿Está seguro que desea guardar los datos?')){
//            return false;  
//        }
        return true;
    } else {
        return false;
    }
}


function validarNumMalformaciones() {
    var tieneMalformacion = document.getElementById("form_cont:contTab:inhTieneMalformacion").value;

    if (tieneMalformacion === 'true') {
        var seleccionadas = document.getElementById("form_cont:contTab:inhNumSeleccionadas").value;
        if (parseInt(seleccionadas) === 0) {
            var msg = document.getElementById("form_cont:contTab:NumMalformacionesMsg");
            var txtMsg = document.getElementById("form_cont:contTab:txtNumMalformacionesMsg");
            if (txtMsg !== null) {
                msg.removeChild(txtMsg);
            }
            var span = document.createElement('span');
            span.setAttribute("id", "form_cont:contTab:txtNumMalformacionesMsg");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El Número de Malformaciones Congénitas debe ser mayor a 0";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }

        var tipoMalfo = document.getElementById("form_cont:contTab:inhMalformacion").value;

        if (tipoMalfo === 'MULTIPLE') {
            if (parseInt(seleccionadas) === 1) {
                var msg = document.getElementById("form_cont:contTab:NumMalformacionesMsg");
                var txtMsg = document.getElementById("form_cont:contTab:txtNumMalformacionesMsg");
                if (txtMsg !== null) {
                    msg.removeChild(txtMsg);
                }
                var span = document.createElement('span');
                span.setAttribute("id", "form_cont:contTab:txtNumMalformacionesMsg");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "Debe Seleccione más de una  Malformación Congénita";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        }

    }
    return "T";
}


function saveN() {
//    if (!confirm('¿Está seguro que desea guardar el registro?')){
//        return false;  
//    }
    return true;
}

function saveD() {
//    if (!confirm('¿Está seguro que desea guardar el registro?')){
//        return false;  
//    }
    return true;
}

/*Para el nombre del guagua*/
function asignarNombre() {
    var nombre = document.getElementById('form_cont:contTab:nombrNacViv').value;
    if (nombre === "") {
        document.getElementById('form_cont:contTab:nombrNacViv').value = "NN";
    }
}
function asignarNombreFocus() {
    var nombre = document.getElementById('form_cont:contTab:nombrNacViv').value;
    if (nombre === "") {
        document.getElementById('form_cont:contTab:nombrNacViv').value = "";
    }
}

/****************************************************************/
/*a partir de aquí está la lógica de validación para anulaciones*/
/****************************************************************/

function valSemGestacionAnulacion() {
    var sg = document.getElementById("renderizaComponentes:1:semanGstcnNacViv").value;
    var msg = document.getElementById("renderizaComponentes:1:semanGstcnNacVivMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgSemGestacion");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(sg >= 22 && sg <= 42)) {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgSemGestacion");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de semanas de gestación no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valContPrenatalesAnulacion() {
    //alert('entra al valContPrenatalesAnulacion')
    var cp = document.getElementById("renderizaComponentes:1:cntrlPrntlNacViv").value;
    //alert('entra al valContPrenatalesAnulacion cp:'+cp)
    var msg = document.getElementById("renderizaComponentes:1:cntrlPrntlNacVivMsg");
    //alert('entra al valContPrenatalesAnulacion msg'+msg)
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgContPrenatales");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (cp > 25) {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgContPrenatales");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de controles prenatales no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosVivSistemaAnulacion() {
    var hv = document.getElementById("renderizaComponentes:1:hijosNacierVivSistema").value;
    var msg = document.getElementById("renderizaComponentes:1:hijosNacierVivSistemaMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgHijosVivSistema");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(hv >= 1 && hv <= 10)) {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosVivSistema");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de hijos que nacieron vivos en este parto no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosMuertSistemaAnulacion() {
    var hm = document.getElementById("renderizaComponentes:1:hijosNacierMuertSistema").value;
    var msg = document.getElementById("renderizaComponentes:1:hijosNacierMuertSistemaMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgHijosMuertSistema");
    //
    var hv = document.getElementById("renderizaComponentes:1:hijosNacierVivSistema").value;
    var element = document.getElementById("renderizaComponentes:1:fkIdProEmb_input");
    var pe = element.options[element.selectedIndex].value;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (hv !== "" && pe !== "" && hm !== "") {
        if (!(hm >= 0 && hm <= 9)) {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuertSistema");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos que nacieron muertos en este parto no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        } else {
            if ((parseInt(hv) + parseInt(hm)) !== parseInt(pe)) {
                var span = document.createElement('span');
                span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuertSistema");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "La suma de hijos nacidos vivos y nacidos muertos correspondientes a este parto debe ser igual al producto del embarazo.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        }
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuertSistema");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "Primero debe ingresar los valores de producto del embrazo, número de hijos vivos y número de hijos muertos de este parto.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valNumeroPartoAnulacion() {
    var nps = document.getElementById("renderizaComponentes:1:numPartoSistema").textContent;
    var np = document.getElementById("renderizaComponentes:1:numerPartoNacViv").value;
    var msg = document.getElementById("renderizaComponentes:1:numerPartoNacVivMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgNumeroParto");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(np) >= parseInt(nps)) {
        if (!(np >= 1 && np <= 19)) {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgNumeroParto");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de partos no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
        document.getElementById("renderizaComponentes:1:hijosVivsaMad").disabled = false;
        document.getElementById("renderizaComponentes:1:hijosNmrtsMad").disabled = false;
        //document.getElementById("form_cont:contTab:hijosVivsaMad").focus();
        return "T";
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgNumeroParto");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de partos históricos debe ser mayor o igual al número del parto actual.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        //encero los datos nuevamente
        document.getElementById("renderizaComponentes:1:hijosVivsaMad").value = "1";
        document.getElementById("renderizaComponentes:1:hijosNmrtsMad").value = "0";
        //Desabilito las cajas de texto de los datos demas datos historicos
        document.getElementById("renderizaComponentes:1:hijosVivsaMad").disabled = true;
        document.getElementById("renderizaComponentes:1:hijosNmrtsMad").disabled = true;
        //document.getElementById("form_cont:contTab:numerPartoNacViv").focus();
        return "F";
    }
}

function valNumEmbarazosAnulacion() {
    var ne = document.getElementById("renderizaComponentes:1:numeroEmbarazosMad").value;
    var np = document.getElementById("renderizaComponentes:1:numerPartoNacViv").value;
    var msg = document.getElementById("renderizaComponentes:1:numeroEmbarazosMadMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgNumEmbarazos");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(ne) >= parseInt(np)) {
        if (!(ne >= 1 && ne <= 38)) {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgNumEmbarazos");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de embarazos no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
        return "T";
    } else {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgNumEmbarazos");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de embarazos debe ser mayor o igual al número de parto del sistema.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
}

function valHijosVivAnulacion() {
    var nps = document.getElementById("renderizaComponentes:1:numPartoSistema").textContent;
    var np = document.getElementById("renderizaComponentes:1:numerPartoNacViv").value;
    var hvs = document.getElementById("renderizaComponentes:1:hijosNacierVivSistema").value;
    //
    var hv = document.getElementById("renderizaComponentes:1:hijosVivsaMad").value;
    var msg = document.getElementById("renderizaComponentes:1:hijosVivsaMadMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgHijosViv");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    //Valido el numero de parto
    if (parseInt(np) >= parseInt(nps)) {
        if (parseInt(hv) >= parseInt(hvs)) {
            if (!(hv >= 1 && hv <= 19)) {
                var span = document.createElement('span');
                span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosViv");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos históricos debe ser mayor o igual al número actual de hijos vivos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else {
        if (!(hv >= 1 && hv <= 19)) {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos que tiene actualmente no es válido.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}

function valHijosVivHanMuertAnulacion() {
    var hv = document.getElementById("renderizaComponentes:1:hijosNvmrtMad").value;
    var msg = document.getElementById("renderizaComponentes:1:hijosNvmrtMadMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgHijosVivHanMuert");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (!(hv >= 0 && hv <= 18)) {
        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosVivHanMuert");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "El número de hijos que nacieron vivos y han muerto no es válido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return "T";
}

function valHijosMuertAnulacion() {
    var nps = document.getElementById("renderizaComponentes:1:numPartoSistema").textContent;
    var np = document.getElementById("renderizaComponentes:1:numerPartoNacViv").value;
    var hms = document.getElementById("renderizaComponentes:1:hijosNacierMuertSistema").value;
    //
    var hm = document.getElementById("renderizaComponentes:1:hijosNmrtsMad").value;
    var msg = document.getElementById("renderizaComponentes:1:hijosNmrtsMadMsg");
    var txtMsg = document.getElementById("renderizaComponentes:1:txtMsgHijosMuert");
    //
    var hv = document.getElementById("renderizaComponentes:1:hijosVivsaMad").value;
    var hvhm = document.getElementById("renderizaComponentes:1:hijosNvmrtMad").value;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (parseInt(np) >= parseInt(nps)) {
        if (parseInt(hm) >= parseInt(hms)) {
            if (np !== "" && hv !== "" && hvhm !== "" && hm !== "") {
                if (!(hm >= 0 && hm <= 18)) {
                    var span = document.createElement('span');
                    span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
                    span.setAttribute("class", "ui-message-error-detail");
                    span.textContent = "El número de hijos que nacieron muertos no es válido.";
                    msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                    msg.appendChild(span);
                    return "F";
                } else {
                    if (parseInt(np) > (parseInt(hv) + parseInt(hvhm) + parseInt(hm))) {
                        var span = document.createElement('span');
                        span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
                        span.setAttribute("class", "ui-message-error-detail");
                        span.textContent = "La suma de hijos vivos, hijos vivos que han muerto e hijos que nacieron muertos debe ser mayor o igual al número del parto.";
                        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                        msg.appendChild(span);
                        return "F";
                    }
                }
            } else {
                var span = document.createElement('span');
                span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "Primero debe ingresar los valores de número de parto, hijos vivos que tiene actualmente, hijos vivos que han muerto e hijos que nacieron muertos.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos que nacieron muertos históricos debe ser mayor o igual al número actual de hijos que nacieron muertos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else {
        if (np !== "" && hv !== "" && hvhm !== "" && hm !== "") {
            if (!(hm >= 0 && hm <= 18)) {
                var span = document.createElement('span');
                span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
                span.setAttribute("class", "ui-message-error-detail");
                span.textContent = "El número de hijos que nacieron muertos no es válido.";
                msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                msg.appendChild(span);
                return "F";
            } else {
                if (parseInt(np) > (parseInt(hv) + parseInt(hvhm) + parseInt(hm))) {
                    var span = document.createElement('span');
                    span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
                    span.setAttribute("class", "ui-message-error-detail");
                    span.textContent = "La suma de hijos vivos, hijos vivos que han muerto e hijos que nacieron muertos debe ser mayor o igual al número del parto.";
                    msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
                    msg.appendChild(span);
                    return "F";
                }
            }
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "Primero debe ingresar los valores de número de parto, hijos vivos que tiene actualmente, hijos vivos que han muerto e hijos que nacieron muertos.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}

function valPartoUnoAnulacion() {
    var nps = document.getElementById("renderizaComponentes:1:numPartoSistema").textContent;
    var hvs = document.getElementById("renderizaComponentes:1:hijosNacierVivSistema").value;
    var hms = document.getElementById("renderizaComponentes:1:hijosNacierMuertSistema").value;

    var np = document.getElementById("renderizaComponentes:1:numerPartoNacViv").value;
    //Hijos vivos histórico
    var msgHv = document.getElementById("renderizaComponentes:1:hijosVivsaMadMsg");
    var txtMsgHv = document.getElementById("renderizaComponentes:1:txtMsgHijosViv");
    var hv = document.getElementById("renderizaComponentes:1:hijosVivsaMad").value;
    //Hijos vivos han muerto histórico
    var msgHvhm = document.getElementById("renderizaComponentes:1:hijosNvmrtMadMsg");
    var txtMsgHvhm = document.getElementById("renderizaComponentes:1:txtMsgHijosVivHanMuert");
    var hvhm = document.getElementById("renderizaComponentes:1:hijosNvmrtMad").value;
    //Hijos muertos histórico
    var msgHm = document.getElementById("renderizaComponentes:1:hijosNmrtsMadMsg");
    var txtMsgHm = document.getElementById("renderizaComponentes:1:txtMsgHijosMuert");
    var hm = document.getElementById("renderizaComponentes:1:hijosNmrtsMad").value;
    //Elimino los mensajes de error
    if (txtMsgHv !== null) {
        msgHv.removeChild(txtMsgHv);
    }
    if (txtMsgHvhm !== null) {
        msgHvhm.removeChild(txtMsgHvhm);
    }
    if (txtMsgHm !== null) {
        msgHm.removeChild(txtMsgHm);
    }
    var comprobacion1 = "T";
    var comprobacion2 = "T";
    var comprobacion3 = "T";
    if (parseInt(np) === parseInt("1")
            && parseInt(nps) === parseInt("1")) {

        if (parseInt(hvs) === parseInt(hv)) {
            comprobacion1 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosViv");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos históricos debe ser igual al número de hijos vivos del parto actual.";
            msgHv.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHv.appendChild(span);
            comprobacion1 = "F";
        }
        if (parseInt(hms) === parseInt(hm)) {
            comprobacion2 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos muertos históricos debe ser igual al número de hijos muertos del parto actual.";
            msgHm.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHm.appendChild(span);
            comprobacion2 = "F";
        }
        if (parseInt(hvhm) === parseInt("0")) {
            comprobacion3 = "T";
        } else {
            var span = document.createElement('span');
            span.setAttribute("id", "renderizaComponentes:1:txtMsgHijosVivHanMuert");
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El número de hijos vivos que han muerto debe ser igual a cero.";
            msgHvhm.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msgHvhm.appendChild(span);
            comprobacion3 = "F";
        }
        //Comprobación Final
        if (comprobacion1 === "T" && comprobacion2 === "T"
                && comprobacion3 === "T") {
            return "T";
        } else {
            return "F";
        }
    }
    return "T";
}

function saveMAnulacion() {
    var element = document.getElementById("renderizaComponentes:0:fkIdEstCivMad_input");
    var pe = element.options[element.selectedIndex].value;
    if (pe == "") {

        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:0:txtMsgfkIdEstCivMad_input");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = ">>>>Debe seleccionar el estado civil";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
    }

    var element1 = document.getElementById("renderizaComponentes:0:fkIdIdeEtnMad_input");
    var pe1 = element.options[element1.selectedIndex].value;
    if (pe1 == "") {

        var span = document.createElement('span');
        span.setAttribute("id", "renderizaComponentes:0:txtMsgfkIdIdeEtnMad_input");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = ">>>>Debe seleccionar la identidad étnica";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
    }

    //alert('entra a saveMAnulacion');
    var vcp = valContPrenatalesAnulacion();
    //alert('1 vcp:'+vcp);
    var vhvs = valHijosVivSistemaAnulacion();
    //alert('2 vhvs:'+vhvs);
    var vhms = valHijosMuertSistemaAnulacion();
    //alert('3 vhms:'+vhms);
    var vnp = valNumeroPartoAnulacion();
    //alert('4 vnp:'+vnp);
    var vhv = valHijosVivAnulacion();
    //alert('5 vhv:'+vhv);
    var vhvhm = valHijosVivHanMuertAnulacion();
    //alert('6 vhvhm:'+vhvhm);
    var vhm = valHijosMuertAnulacion();
    //alert('7 vhm:'+vhm);
    var vsg = valSemGestacionAnulacion();
    //alert('8 vsg:'+vsg);
    var vne = valNumEmbarazosAnulacion();
    //alert('9 vne:'+vne);
    var vpu = valPartoUnoAnulacion();
    //alert('10 vpu:'+vpu);
    //alert('FINAL vcp:'+vcp);
    if (vcp === 'T' && vhvs === 'T' && vhms === 'T'
            && vnp === 'T' && vhv === 'T'
            && vhvhm === 'T' && vhm === 'T' && vsg === 'T' && vne === 'T'
            && vpu === 'T') {
        if (!confirm('¿Está seguro que desea editar el registro?')) {
            return false;
        }
        return true;
    } else {
        return false;
    }
}

/****************************************************************/
/*a partir de aquí está la lógica de validación para defunciones*/
/****************************************************************/

function valUnidadMedida1() {
    alert("entra");
}

function valUnidadMedida(idComponente, idMsg, idTxtMsg, idMedida) {
    var componente = document.getElementById(idComponente).value;
    var msg = document.getElementById(idMsg);
    var txtMsg = document.getElementById(idTxtMsg);
    var element = document.getElementById(idMedida);
    var medida = element.options[element.selectedIndex].value;
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (medida === 'MINUTOS') {
        if (!(componente >= 1 && componente <= 59)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 60.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else if (medida === 'HORAS') {
        if (!(componente >= 1 && componente <= 23)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 24.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else if (medida === 'DÍAS') {
        if (!(componente >= 1 && componente <= 29)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 31.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else if (medida === 'SEMANAS') {
        if (!(componente >= 1 && componente <= 3)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 4.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else if (medida === 'MESES') {
        if (!(componente >= 1 && componente <= 11)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 12.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    } else if (medida === 'AÑOS') {
        if (!(componente >= 1 && componente <= 115)) {
            var span = document.createElement('span');
            span.setAttribute("id", idTxtMsg);
            span.setAttribute("class", "ui-message-error-detail");
            span.textContent = "El valor permitido es entre 1 y 20.";
            msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
            msg.appendChild(span);
            return "F";
        }
    }
    return "T";
}


function saveDF(idComponentea, idMsga, idTxtMsga, idMedidaa,
        idComponenteb, idMsgb, idTxtMsgb, idMedidab,
        idComponentec, idMsgc, idTxtMsgc, idMedidac,
        idComponented, idMsgd, idTxtMsgd, idMedidad,
        idComponenteo, idMsgo, idTxtMsgo, idMedidao) {
    var cma = valUnidadMedida(idComponentea, idMsga, idTxtMsga, idMedidaa);
    var cmb = valUnidadMedida(idComponenteb, idMsgb, idTxtMsgb, idMedidab);
    var cmc = valUnidadMedida(idComponentec, idMsgc, idTxtMsgc, idMedidac);
    var cmd = valUnidadMedida(idComponented, idMsgd, idTxtMsgd, idMedidad);
    var cmo = valUnidadMedida(idComponenteo, idMsgo, idTxtMsgo, idMedidao);
    if (cma === 'T' && cmb === 'T' && cmc === 'T'
            && cmd === 'T' && cmo === 'T') {
        if (!confirm('¿Está seguro que desea guardar el registro?')) {
            return false;
        }
        return true;
    } else {
        return false;
    }
}

//valida nombre de la madre
function valNomMadre() {
    var nm = document.getElementById("form_cont:contTab:nombreUno").value;
    var msg = document.getElementById("form_cont:contTab:nombreUnoMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgNomMad");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (nm.toLowerCase() === "sin nombre" || nm.toLowerCase() === "sinnombre" || nm.toLowerCase() === "sin apellido" || nm.toLowerCase() === "sinapellido" || nm.toLowerCase() === "indocumentada" || nm.toLowerCase() === "na" ||
            nm.toLowerCase() === "n/a" || nm.toLowerCase() === "no especificado" || nm.toLowerCase() === "noespercificado" || nm.toLowerCase() === "descconocido" || nm.toLowerCase() === "extranjera" || nm.toLowerCase() === "EXTRANJERA")
    {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgNomMad");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "Nombre no valido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return  "T";
}

//Valida apellido de la madre
function valApellMadre() {
    var am = document.getElementById("form_cont:contTab:apellidosUno").value;
    var msg = document.getElementById("form_cont:contTab:apellidosUnoMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgApellMad");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (am.toLowerCase() === "sin nombre" || am.toLowerCase() === "sinnombre" || am.toLowerCase() === "sin apellido" || am.toLowerCase() === "sinapellido" || am.toLowerCase() === "indocumentada" || am.toLowerCase() === "na" ||
            am.toLowerCase() === "n/a" || am.toLowerCase() === "no especificado" || am.toLowerCase() === "noespercificado" || am.toLowerCase() === "descconocido" || am.toLowerCase() === "extranjera" || am.toLowerCase() === "EXTRANJERA")
    {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgApellMad");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "Apellido no valido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";
    }
    return  "T";


}

function valNomApellMad() {
    var am = document.getElementById("form_cont:contTab:nombrMad").value;
    var msg = document.getElementById("form_cont:contTab:nombreApellMsg");
    var txtMsg = document.getElementById("form_cont:contTab:txtMsgNomApellMad");
    if (txtMsg !== null) {
        msg.removeChild(txtMsg);
    }
    if (am.toLowerCase() === "sin nombre" || am.toLowerCase() === "sinnombre" || am.toLowerCase() === "sin apellido" || am.toLowerCase() === "sinapellido" || am.toLowerCase() === "indocumentada" || am.toLowerCase() === "na" ||
            am.toLowerCase() === "n/a" || am.toLowerCase() === "no especificado" || am.toLowerCase() === "noespercificado" || am.toLowerCase() === "descconocido" || am.toLowerCase() === "extrangera" || am.toLowerCase() === "EXTRANJERA")
    {
        var span = document.createElement('span');
        span.setAttribute("id", "form_cont:contTab:txtMsgNomApellMad");
        span.setAttribute("class", "ui-message-error-detail");
        span.textContent = "Apellido y nombre no valido.";
        msg.setAttribute("class", "ui-message ui-message-error ui-widget ui-corner-all");
        msg.appendChild(span);
        return "F";

    }
    return  "T";



}