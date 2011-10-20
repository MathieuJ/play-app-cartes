// FONCTIONS STABLES
function afficheCarte(url) {
  var objCible = document.getElementById('macarte');
  //objCible.style.backgroundImage = 'url("' + url + '")';
  objCible.src = url;
}

function afficheOptionsJoueurs(numJoueur, nomJoueur, pvJoueur) {
  $("#dialog").attr("title", nomJoueur);
  $("#dialog")
      .html(
          '<p>'
              + nomJoueur
              + ' : <input readonly type="text" id="nbPv" size=3 style="border:0; color:#f6931f; font-weight:bold;" /> points de Vie</p>'
              + '<div id="slider-nbPv"></div>');
  $("#dialog").dialog({
    resizable : false,
    modal : true,
    height : 250,
    width : 450,
    buttons : {
      "Modifier" : function() {
        actionGenerale("modifiePV", numJoueur, $("#nbPv").val());
        $(this).dialog("close");
      },
      "Annuler" : function() {
        $(this).dialog("close");
      }
    }
  });
  $(function() {
    $("#slider-nbPv").slider({
      range : "min",
      value : pvJoueur,
      min : 0,
      max : 30,
      slide : function(event, ui) {
        $("#nbPv").val(ui.value);
      }
    });
    $("#nbPv").val($("#slider-nbPv").slider("value"));
  });
}

function afficheOptionsToken() {
  $("#dialog").attr("title", "Créer un Token");
  $("#dialog").html(
      '<SELECT id="selectToken" size="10">' + '<OPTION value="255">Angel'
          + '<OPTION value="256">Demon' + '<OPTION value="257">Homonculus'
          + '<OPTION value="258">Ooze' + '<OPTION value="259">Spider'
          + '<OPTION value="260">Spirit' + '<OPTION value="261">Vampire'
          + '<OPTION value="262">Black Wolf' + '<OPTION value="263">Green Wolf'
          + '<OPTION value="264">Zombie' + '</SELECT>');
  $("#dialog").dialog({
    resizable : false,
    modal : true,
    height : 400,
    width : undefined,
    buttons : {
      "Ok" : function() {
        actionGenerale("creeToken", $("#selectToken").val());
        $(this).dialog("close");
      },
      "Annuler" : function() {
        $(this).dialog("close");
      }
    }
  });
}

function afficheOptionsBibliotheque() {
  $("#dialog").attr("title", "Bibliothèque");
  $("#dialog").html("Manipulation de bibliotheque");
  $("#dialog").dialog({
    resizable : false,
    modal : true,
    buttons : {
      "Piocher une carte" : function() {
        actionGenerale("pioche", "Bibliotheque_1", "Main_1", 1, null);
        $(this).dialog("close");
      },
      "Afficher la bibliothèque" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee('Bibliotheque_1');
      },
      "Annuler" : function() {
        $(this).dialog("close");
      }
    }
  });
}

$(function() {

  $("#test2").prettypiemenu({
    buttons : [ {
      img : "ui-icon-minus",
      title : "plaah1"
    }, {
      img : "ui-icon-plus",
      title : "plaah2"
    }, {
      img : "ui-icon-close",
      title : "plaah3"
    } ],
    onSelection : function(item) {
      alert(item + ' was clicked!');
    },
    showTitles : true
  });

  $("#test").prettypiemenu({
    buttons : [ {
      img : "ui-icon-minus",
      title : "plaah1"
    }, {
      img : "ui-icon-plus",
      title : "plaah2"
    }, {
      img : "ui-icon-close",
      title : "plaah3"
    }, {
      img : "ui-icon-check",
      title : "plaah4"
    } ],
    onSelection : function(item) {
      alert(item + ' was clickedoo!');
    },
    showTitles : true
  });

  $("#testbtn").button({
    icons : {
      primary : "ui-icon-gear"
    },
    text : false
  }).click(function(event) {
    event.preventDefault();
    var offset = $("#testbtn").offset();
    var h = $("#testbtn").height();
    var w = $("#testbtn").width();
    var btn_middle_y = offset.top + h / 2;
    var btn_middle_x = offset.left + w / 2;
    $("#test").prettypiemenu("show", {
      top : btn_middle_y,
      left : btn_middle_x
    });
    return false;
  });
});

function afficheOptions(zone, x, y) {
  var dialogTitre;
  var dialogHtml;
  var dialogBoutons = {};

  switch (zone) {
  case "Bibliotheque_" + soi:
    dialogTitre = "Bibliothèque";
    dialogHtml = "";
    dialogBoutons = {
      "Piocher une carte" : function() {
        actionGenerale("pioche", "true", 1);
        $(this).dialog("close");
      },
      "Piocher une main" : function() {
        actionGenerale("pioche", "true", 7);
        $(this).dialog("close");
      },
      "Meuler X cartes" : function() {
        actionGenerale("meule", "true", 5);
        $(this).dialog("close");
      },
      "Afficher la bibliothèque" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Bibliotheque", "true", "tout");
      },
      "Afficher X cartes du dessus" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Bibliotheque", "true", "top", 3);
      },
      "Afficher X cartes du dessous" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Bibliotheque", "true", "bottom", 3);
      }
    };
    break;
  case "Bibliotheque_" + adversaire:
    dialogTitre = "Bibliothèque de l'adversaire";
    dialogHtml = "";
    dialogBoutons = {
      "Piocher une carte" : function() {
        actionGenerale("pioche", "false", 1);
        $(this).dialog("close");
      },
      "Meuler X cartes" : function() {
        actionGenerale("meule", "false", 5);
        $(this).dialog("close");
      },
      "Afficher la bibliothèque" : function() {
      }
    };
    break;
  case "Cimetiere_" + soi:
    dialogTitre = "Cimetière";
    dialogHtml = "";
    dialogBoutons = {
      "Melanger dans la bibliothèque" : function() {
        actionGenerale("melange", "Cimetiere", "Bibliotheque", "true");
        $(this).dialog("close");
      },
      "Afficher le cimetiere" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Cimetiere", "true", "tout");
      }
    };
    break;
  case "Main_1":
  case "Main_2":
    dialogTitre = "Jeu";
    dialogHtml = "";
    dialogBoutons = {
      "Degager tout" : function() {
        actionGenerale("degageTout", "true");
        $(this).dialog("close");
      },
      "Créer un token" : function() {
        $(this).dialog("close");
        afficheOptionsToken();
      },

      "Fin de tour" : function() {
        actionGenerale("prochaineEtape", 0);
        $(this).dialog("close");
      },

      "Redemarrer" : function() {
        actionGenerale("reset");
        $(this).dialog("close");
      }
    };
    break;
  }

  dialogBoutons['Annuler'] = function() {
    $(this).dialog("close");
  };

  $("#dialog").dialog({
    title : dialogTitre,
    html : dialogHtml,
    height : undefined,
    width : undefined,
    resizable : false,
    modal : true,
    buttons : dialogBoutons
  });
}

function afficheChiffre(action, min, max, defaut) {
  /*
   * $("#dialog").attr("title", action); $("#dialog").html(dialogHtml);
   * $("#dialog").dialog({ resizable : false, modal : true, buttons :
   * dialogBoutons });
   */
}
function afficheZoneEmpilee(zone, soi, type, nombre) {
  var loadZoneUrl = loadZoneAction({
    zoneNom : zone,
    soi : soi,
    param1 : type,
    param2 : nombre
  });

  $.get(loadZoneUrl, function(data) {
    $("#dialog").attr("title", zone);
    $("#dialog").html(data);
    $("#dialog").dialog({
      title : zone,
      html : data,
      resizable : false,
      height : 400,
      width : 600,
      position : 'left',
      modal : true,
      buttons : {
        "Mettre en jeu" : function() {
          var selection = [];
          $.each($("#dialog > .selectionnee"), function(index, value) {
            selection.push(value.id);
            // alert(index + '_' + value + '_' + value.id);
          });
          actionGenerale('deplacePaquet', zone, "ChampBataille", selection);
          $(this).dialog("close");
        },
        "Mettre en main" : function() {
          var selection = [];
          $.each($("#dialog > .selectionnee"), function(index, value) {
            selection.push(value.id);
            // alert(index + '_' + value + '_' + value.id);
          });
          actionGenerale('deplacePaquet', zone, "Main", selection);
          $(this).dialog("close");
        },
        "Annuler" : function() {
          $(this).dialog("close");
          $(".petiteCarte").unbind('click');
        }
      }
    });
  });
}

function afficheOptionsCarte(zone, carteNom, carteId) {
  var dialogTitre;
  var dialogHtml;
  var dialogBoutons = {};

  switch (zone) {
  case "Main_Soi":
    dialogTitre = "Carte : " + carteNom
    dialogHtml = "";
    dialogBoutons = {
      "Jouer" : function() {
        actionGenerale('deplace', carteId, "Main", "ChampBataille");
        $(this).dialog("close");
      },
      "Attacher" : function() {
        actionGenerale('attache', carteId, zone);
        $(this).dialog("close");
      },
      "Défausser" : function() {
        actionGenerale('deplace', carteId, "Main", "Cimetiere");
        $(this).dialog("close");
      },
      "Spécial" : function() {
        $(this).dialog("close");
      }
    }
    break;
  case "ChampBataille_Soi":
    dialogTitre = "Carte : " + carteNom;
    dialogHtml = "";
    dialogBoutons = {
      "Engager : Utiliser une capacité" : function() {
        actionGenerale('engagecapacite', carteId);
        $(this).dialog("close");
      },
      "Utiliser une capacité" : function() {
        actionGenerale('capacite', carteId);
        $(this).dialog("close");
      },
      "Engager / Degager" : function() {
        actionGenerale('attaque', carteId);
        $(this).dialog("close");
      },
      "Sacrifier" : function() {
        actionGenerale('deplace', carteId, "ChampBataille", "Cimetiere");
        $(this).dialog("close");
      },
      "Détruire" : function() {
        actionGenerale('deplace', carteId, "ChampBataille", "Cimetiere");
        $(this).dialog("close");
      },
      "Renvoie en main" : function() {
        actionGenerale('deplace', carteId, "ChampBataille", "Main");
        $(this).dialog("close");
      },
      "Transforme" : function() {
        actionGenerale('transforme', carteId);
        $(this).dialog("close");
      },
      "Spécial..." : function() {
        $(this).dialog("close");
      }
    }
    break;
  }

  dialogBoutons['Annuler'] = function() {
    $(this).dialog("close");
  };

  $("#dialog").dialog({
    title : dialogTitre,
    html : dialogHtml,
    resizable : false,
    height : undefined,
    width : undefined,
    modal : true,
    buttons : dialogBoutons
  });
}

function actionGenerale(nomAction, param1, param2, param3, param4) {
  var actionGeneraleUrl = doActionGenerale({
    nomAction : nomAction,
    param1 : param1,
    param2 : param2,
    param3 : param3,
    param4 : param4
  });
  $('#logs').append('user ' + actionGeneraleUrl + ' ' + nomAction);
  $.post(actionGeneraleUrl);
}

var lastReceived = 0;

function envoiTxt() {
  var message = $('#message').val();
  $('#message').val('');
  $('#logs').prepend(lastReceived + ' envoi message : ' + message);
  $.post(say(), {
    message : message
  });
}

function envoi(txt) {
  $('#logs').prepend(lastReceived + ' envoi message prefait : ' + txt);
  $.post(say(), {
    message : txt
  });
}

function updatePartie() {
  $('#logs').prepend("update...<BR/>");
  var updatePartieUrl = doUpdatePartie({});
  $.get(updatePartieUrl, function(data) {
    $('#logs').prepend("update done!<BR/>");
    $("#partie").html(data);
  });
}

// Retrieve new messages
function getMessages() {
  $('#logs').prepend("Get messages<BR/>");
  $.ajax({
    url : waitMessages({
      lastReceived : lastReceived
    }),
    success : function(events) {
      var update = false;
      $('#logs').prepend("reception...<BR/>");
      $(events).each(function() {
        display(this.data);
        lastReceived = this.id;
        update = (this.data.type == 'actionGenerale');
      })
      $('#logs').prepend("done!<BR/>");
      if (update)
        updatePartie();
      getMessages();
    },
    dataType : 'json'
  });
}

// Display a message
var display = function(event) {
  $('#logs').append(event.type + "<BR/>");
  var classe = "message";
  if (typeof (soi) != "undefined" && event.numUser == soi) {
    classe += " vous";
  }
  if (event.type == 'actionGenerale') {
    classe += " action";
  }
  $('#logs').append(event + "<BR/>");
  $('#thread').append(
      "<div class='" + classe + "'><p>" + event.user + ':' + event.texte
          + "</p></div>");
  $('#thread').scrollTo('max');
}

// $('.engagee').rotate(90);
