
/* global PrimeFaces */

function validateIntegerValueAndFocusNext(event, next) {
    var keyCode = event.which || event.keyCode;
    //console.log(keyCode);
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();
        }
    }

    if (keyCode < 48) {
        if (keyCode !== 8 && keyCode !== 9 && keyCode !== 37 && keyCode !== 39) {
            event.preventDefault();
            return false;
        }

    }
    if (keyCode > 57) {
        event.preventDefault();
        return false;
    }

    return true;
}
function validateFloatValueAndFocusNext(event, next) {
    var keyCode = event.which || event.keyCode;
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();
        }
    }
    if (keyCode > 57) {
        event.preventDefault();
        return false;
    }
    if (keyCode < 48) {
        /*. 46  ,44*/
        if (keyCode !== 8 && keyCode !== 46 && keyCode !== 9 && keyCode !== 37 && keyCode !== 39 && keyCode !== 44) {
            event.preventDefault();
            return false;
        }
    }
    return true;
}

function focusNextOnEnter(event, next) {
    var keyCode = event.which || event.keyCode;
    //console('keyCode');
    console.log(keyCode);
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        //console.log(next);
        var element = $(next);
        if (element) {
            element.focus();
            element.select();
            event.preventDefault();
        }
    }
    return true;
}

function focusNextOnEnterProforma(event, next, tipo) {
    var keyCode = event.which || event.keyCode;
    //console.log(keyCode);
    //console.log(tipo);
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        //console.log(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();
            event.preventDefault();
        }
        //SOLICITANTE : 1
        //BENEFICIARIO : 2
        //NADA : 3
        //FICHAS : 4
        if (tipo === 1) {
            buscar();
        }
        if (tipo === 2) {
            buscarBeneficiario();
        }

        if (tipo === 4) {
            showDlgFichas();
        }
        return false;
    }
    return false;
}


function focusNextOnEnterFicha(event, next) {
    document.activeElement.blur();
    next = PrimeFaces.escapeClientId(next);
    console.log(next);
    var element = $(next);
    if (element)
    {
        element.focus();
        element.select();
        event.preventDefault();
    }
    return false;

}


function showCurrentCellActo() {
    console.log('hoooli desde showCurrentCell');
    $(document).ready(function () {
        //Fix for primefaces bug: https://github.com/primefaces/primefaces/issues/3437
        PrimeFaces.widget.DataTable.prototype.showCurrentCell = function (cell) {
            var $this = this;

            if (this.currentCell) {
                if (this.cfg.saveOnCellBlur)
                    this.saveCell(this.currentCell);
                else if (!this.currentCell.is(cell))
                    this.doCellEditCancelRequest(this.currentCell);
            }
            if (cell && cell.length) {
                this.currentCell = cell;

                var cellEditor = cell.children('div.ui-cell-editor'),
                        displayContainer = cellEditor.children('div.ui-cell-editor-output'),
                        inputContainer = cellEditor.children('div.ui-cell-editor-input'),
                        inputs = inputContainer.find(':input:enabled'),
                        multi = inputs.length > 1;

                cell.addClass('ui-state-highlight ui-cell-editing');
                displayContainer.hide();
                inputContainer.show();
                inputs.eq(0).focus().select();

                //metadata
                if (multi) {
                    var oldValues = [];
                    for (var i = 0; i < inputs.length; i++) {
                        var input = inputs.eq(i);

                        if (input.is(':checkbox')) {
                            oldValues.push(input.val() + "_" + input.is(':checked'));
                        } else {
                            oldValues.push(input.val());
                        }
                    }

                    cell.data('multi-edit', true);
                    cell.data('old-value', oldValues);
                } else {
                    cell.data('multi-edit', false);
                    cell.data('old-value', inputs.eq(0).val());
                }

                //bind events on demand
                //if (!cell.data('edit-events-bound')) {
                cell.data('edit-events-bound', true);

                inputs.on('keydown.datatable-cell', function (e) {
                    var keyCode = $.ui.keyCode,
                            shiftKey = e.shiftKey,
                            key = e.which,
                            input = $(this);

                    if (key === keyCode.ENTER || key == keyCode.NUMPAD_ENTER) {
                        var focusIndex = shiftKey ? input.index() - 1 : input.index() + 1;

                        if (focusIndex < 0 || (focusIndex === inputs.length) || input.parent().hasClass('ui-inputnumber') || input.parent().hasClass('ui-helper-hidden-accessible')) {
                            $this.tabCell(cell, !shiftKey);
                        } else {
                            inputs.eq(focusIndex).focus();
                        }
                        e.preventDefault();
                    } else if (key === keyCode.TAB) {
                        if (multi) {
                            var focusIndex = shiftKey ? input.index() - 1 : input.index() + 1;

                            if (focusIndex < 0 || (focusIndex === inputs.length) || input.parent().hasClass('ui-inputnumber') || input.parent().hasClass('ui-helper-hidden-accessible')) {
                                $this.tabCell(cell, !shiftKey);
                            } else {
                                inputs.eq(focusIndex).focus();
                            }
                        } else {
                            $this.tabCell(cell, !shiftKey);
                        }

                        e.preventDefault();
                    } else if (key === keyCode.ESCAPE) {
                        $this.doCellEditCancelRequest(cell);
                        e.preventDefault();
                    }
                })
                        .on('focus.datatable-cell click.datatable-cell', function (e) {
                            $this.currentCell = cell;
                        });
                //}
            } else {
                this.currentCell = null;
            }
        }
        //showCurrentCell END
    });
}

function checkKey(e) {
    var event = window.event ? window.event : e;
    if (event.keyCode == 40) { //down
        var idx = $("tr:focus").attr("data-ri");
        idx++;
        if (idx > 6) {
            idx = 0;
        }
        $("tr[data-ri=" + idx + "]").focus();
    }
    if (event.keyCode == 38) { //up
        var idx = $("tr:focus").attr("data-ri");
        idx--;
        if (idx < 0) {
            idx = 6;
        }
        $("tr[data-ri=" + idx + "]").focus();
    }
}


function focusNextOnEnter(input) {
    var event = window.event;
    var keyCode = event.which || event.keyCode;
    if (keyCode === 13) {
        var id = input.currentTarget.id;
        //console.log(id);
        event.preventDefault();
        // Get all focusable elements on the page
        var $canfocus = $(':focusable');
        var indexe = 0;
        var indexin = 0;
        $canfocus.each(function (e) {
            if ($(this).attr("id") === id) {
                indexin = indexe;
            }
            indexe = indexe + 1;
        });
        $canfocus[indexin + 1].focus();
        $canfocus[indexin + 1].select();
    }
    return true;
}


function wizardTransform() {
    $('ul.ui-wizard-step-titles>li.ui-wizard-step-title').each(function (e) {

        $(this).removeClass('ui-wizard-step-title ui-corner-all ui-state-default');
        $(this).addClass(' origami-wizard');

        $(this).html('<a href="#">' + $(this).html() + '</a>');

    });
}

$(document).ready(function () {

    $(document).on('blur', 'div.compContainer input.compInput', function () {

        var value = $(this).val();
        var id = $(this).attr("id");
        var field = $(this).attr("data-field");
        var defaultValue = $(this).attr("data-default-value");
        var allValues = $(this).attr("data-all-values");
        var valorActualInput = $(this).attr("data-id");

        if (typeof valorActualInput !== "undefined" || value) {
            var idOrden = allValues.split('-');
            var idItem = -1;
            var orden = -1;

            for (var i = 0; i < idOrden.length; i++) {
                var temp = idOrden[i].split(";");
                if (temp[1] === value) {
                    idItem = temp[0];
                    orden = temp[1];
                    break;
                }
            }
            if (orden === -1) {
                value = defaultValue;
                for (var i = 0; i < idOrden.length; i++) {
                    var temp = idOrden[i].split(";");
                    if (temp[1] === value) {
                        idItem = temp[0];
                        orden = temp[1];
                        break;
                    }
                }
            } else {

                value = orden;
            }
            $(this).val(parseInt(value));

            var arr = id.split(':');
            var prefIdSelect = '';

            for (var i = 0; i < arr.length - 1; i++) {
                prefIdSelect += arr[i] + '\\:';
            }
            var idSelect = prefIdSelect + field + '-select_input';
            var idLabel = prefIdSelect + field + '-select_label';


            $("#" + idSelect).val('com.origami.sgm.entities.CtlgItem:' + idItem + ':java.lang.Long');
            $("#" + idLabel).html($("#" + idSelect).find('option:selected').text());
        } else {

            if (value) {
                var idOrden = allValues.split('-');
                var idItem = -1;
                var orden = -1;

                for (var i = 0; i < idOrden.length; i++) {
                    var temp = idOrden[i].split(";");
                    if (temp[1] === value) {
                        idItem = temp[0];
                        orden = temp[1];
                        break;
                    }
                }
                if (orden === -1) {
                    value = defaultValue;
                    for (var i = 0; i < idOrden.length; i++) {
                        var temp = idOrden[i].split(";");
                        if (temp[1] === value) {
                            idItem = temp[0];
                            orden = temp[1];
                            break;
                        }
                    }
                } else {

                    value = orden;
                }
                $(this).val(parseInt(value));

                var arr = id.split(':');
                var prefIdSelect = '';

                for (var i = 0; i < arr.length - 1; i++) {
                    prefIdSelect += arr[i] + '\\:';
                }
                var idSelect = prefIdSelect + field + '-select_input';
                var idLabel = prefIdSelect + field + '-select_label';


                $("#" + idSelect).val('com.origami.sgm.entities.CtlgItem:' + idItem + ':java.lang.Long');
                $("#" + idLabel).html($("#" + idSelect).find('option:selected').text());

            }

        }

    });

    $(document).on('change', 'div.compContainer select', function () {


        var value = $(this).val();
        var id = $(this).attr("id");
        var field = $(this).attr("data-field");

        var arr = id.split(':');
        var prefIdSelect = '';

        for (var i = 0; i < arr.length - 1; i++) {
            prefIdSelect += arr[i] + '\\:';
        }
        var idInput = prefIdSelect + field + '-input';

        var allValues = $(this).attr("data-all-values");

        var idsOrdenItems = allValues.split('-');
        var idItemArray = value.split(':');
        var idItemSeleccionado = idItemArray[1];
        var orden = -1;

        for (var i = 0; i < idsOrdenItems.length; i++) {
            var temp = idsOrdenItems[i].split(";");
            if (temp[0] === idItemSeleccionado) {
                orden = temp[1];
                break;
            }
        }

        $("#" + idInput).val(orden === -1 ? '' : parseInt(orden));

    });
});

function toUpperCase(input, tuUpperRelease) {
    var keynum;
    var e = window.event;
    if (window.event) { // IE                    
        keynum = e.keyCode;
    } else if (e.which) { // Netscape/Firefox/Opera                   
        keynum = e.which;
    }
    if (keynum === 32 || keynum === 8) {
        return;
    }
    var value = input.value;
    if (isNaN(value)) {
        if (tuUpperRelease) {
            var caretPos = input.selectionStart;
            input.value = value.toUpperCase();
            input.selectionStart = caretPos;
            input.selectionEnd = caretPos;
            input.ad = caretPos;
        }
    }
}
function toUpperCase(input) {
    var keynum;
    var e = window.event;
    if (window.event) { // IE                    
        keynum = e.keyCode;
    } else if (e.which) { // Netscape/Firefox/Opera                   
        keynum = e.which;
    }
    if (keynum === 32 || keynum === 8) {
        return;
    }
    var value = input.value;
    if (isNaN(value)) {
        var caretPos = input.selectionStart;
        input.value = value.toUpperCase();
        input.selectionStart = caretPos;
        input.selectionEnd = caretPos;
        input.ad = caretPos;
    }
}


