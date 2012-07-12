// ************************ FONCTIONS STABLES
function afficheCarte(url) {
  var objCible = document.getElementById('macarte');
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
      '<SELECT id="selectToken" size="10">' + '<OPTION value="Angel Token">Angel'
          + '<OPTION value="Demon Token">Demon' + '<OPTION value="Homonculus Token">Homonculus'
          + '<OPTION value="Ooze Token">Ooze' + '<OPTION value="Spider Token">Spider'
          + '<OPTION value="Spirit Token">Spirit' + '<OPTION value="Vampire Token">Vampire'
          + '<OPTION value="Black Wolf Token">Black Wolf' + '<OPTION value="Green Wolf Token">Green Wolf'
          + '<OPTION value="Zombie Token">Zombie' + '</SELECT>');
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

//************************ FIN FONCTIONS STABLES

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

/*$(function() {
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
});*/

function demandeChiffreAction(titre, texte, action, min, max, value) {
  $("#dialog")
  .html(
      '<p>' + texte + '<input readonly type="text" id="nbCartes" size=1 style="border:0; color:#f6931f; font-weight:bold;" /></p>'
          + '<div id="slider-nbCartes"></div>');
  $("#dialog").dialog({
    title : titre,
    height : 300,
    width : 300,
    resizable : false,
    modal : true,
    buttons : {
      "Meuler" : function() {
        actionGenerale(action, "true", $("#nbCartes").val());
        $(this).dialog("close");
      },
      "Annuler" : function() {
        $(this).dialog("close");
      }
    }
  });
  
  $(function() {
    $("#slider-nbCartes").slider({
      range : "min",
      value : value,
      min : min,
      max : max,
      slide : function(event, ui) {
        $("#nbCartes").val(ui.value);
      }
    });
    $("#nbCartes").val($("#slider-nbCartes").slider("value"));
  });
}

function afficheOptions(zone, x, y) {
  var dialogTitre;
  var dialogBoutons = {};

  switch (zone) {
  case "Bibliotheque_" + soi:
    dialogTitre = "Bibliothèque";
    dialogBoutons = {
      "Piocher une carte" : function() {
        actionGenerale("pioche", "true", 1);
        $(this).dialog("close");
      },
      "Meuler X cartes" : function() {
        $(this).dialog("close");
        demandeChiffreAction("Meule", "Nombre de cartes à mettre du dessus de la bibliotheque au cimetiere : ", "meule", 1, 10, 1);
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
    dialogBoutons = {
        "Voir X cartes du dessus" : function() {
          $(this).dialog("close");
          afficheZoneEmpilee("Bibliotheque", "false", "top", 3);
        }
    };
    break;
  case "Cimetiere_" + soi:
    dialogTitre = "Cimetière";
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
  case "Cimetiere_" + adversaire:
    dialogTitre = "Cimetière";
    dialogBoutons = {
      "Afficher le cimetiere" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Cimetiere", "true", "tout");
      }
    };
    break;
  case "Exil_" + soi:
    dialogTitre = "Exil";
    dialogBoutons = {
      "Afficher l'exil" : function() {
        $(this).dialog("close");
        afficheZoneEmpilee("Exil", "true", "tout");
      }
    };
    break;
  case "Main_1":
  case "Main_2":
    dialogTitre = "Jeu";
    dialogBoutons = {
      "Degager tout" : function() {
        actionGenerale("degageTout", "true");
        $(this).dialog("close");
      },
      "Créer un token" : function() {
        $(this).dialog("close");
        afficheOptionsToken();
      },
      "Mulliganer" : function() {
        actionGenerale("mulligan");
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
  $("#dialog").html("");
  $("#dialog").dialog({
    title : dialogTitre,
    height : undefined,
    position : "center",
    width : undefined,
    resizable : false,
    modal : true,
    buttons : dialogBoutons
  });
}

function proposeUntapDraw() {
  dialogTitre = "Votre tour ";
  dialogBoutons = {
    "Dégager et aller à l'etape d'entretien" : function() {
      actionGenerale('goUpkeep');
      $(this).dialog("close");
    },
    "Dégager, piocher et aller à l'étape principale" : function() {
      actionGenerale('goMain');
      $(this).dialog("close");
    }
  }
  $("#dialog").html("");
  $("#dialog").dialog({
    title : dialogTitre,
    position : "center",
    resizable : false,
    height : undefined,
    width : undefined,
    modal : true,
    buttons : dialogBoutons
  });
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
      position : "center",
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
          });
          actionGenerale('deplacePaquet', zone, "ChampBataille", selection);
          $(this).dialog("close");
        },
        "Mettre en main" : function() {
          var selection = [];
          $.each($("#dialog > .selectionnee"), function(index, value) {
            selection.push(value.id);
          });
          actionGenerale('deplacePaquet', zone, "Main", selection);
          $(this).dialog("close");
        },
        "Exiler" : function() {
          var selection = [];
          $.each($("#dialog > .selectionnee"), function(index, value) {
            selection.push(value.id);
          });
          actionGenerale('deplacePaquet', zone, "Exil", selection);
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

function setEtape(etape){
  actionGenerale('setEtape', etape);
}

var modeSelection = false;
var carteAAttacherId;

function afficheOptionsCarte(zone, carteNom, carteId) {
  if (modeSelection) {
    modeSelection = false;
    actionGenerale('attache', carteAAttacherId, carteId);
    return;
  }
  var dialogTitre;
  var dialogBoutons = {};

  switch (zone) {
  case "Main_Soi":
    dialogTitre = "Carte : " + carteNom
    dialogBoutons = {
      "Jouer" : function() {
        actionGenerale('deplace', carteId, "Main", "ChampBataille");
        $(this).dialog("close");
      },
      "Défausser" : function() {
        actionGenerale('deplace', carteId, "Main", "Cimetiere");
        $(this).dialog("close");
      }
    }
    break;
  case "ChampBataille_Soi":
    dialogTitre = "Carte : " + carteNom;
   dialogBoutons = {
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
      "Attacher" : function() {
        modeSelection = true;
        carteAAttacherId = carteId;
        $(this).dialog("close");
        alert("Cliquez sur la carte cible");
      },
      "Detacher" : function() {
        modeSelection = true;
        carteAAttacherId = carteId;
        $(this).dialog("close");
        alert("Cliquez sur la carte cible");
      },
      "Renvoie en main" : function() {
        actionGenerale('deplace', carteId, "ChampBataille", "Main");
        $(this).dialog("close");
      },
      "Transforme" : function() {
        actionGenerale('transforme', carteId);
        $(this).dialog("close");
      }
    }
    break;
  }

  dialogBoutons['Annuler'] = function() {
    $(this).dialog("close");
  };
  $("#dialog").html("");
  $("#dialog").dialog({
    title : dialogTitre,
    position : "center",
    resizable : false,
    height : undefined,
    width : undefined,
    modal : true,
    buttons : dialogBoutons
  });
}

function onClickCarte(zoneNom, carteNom, carteId, isTerrain){
  if (isTerrain && (zoneNom == 'ChampBataille_Soi')) {
    actionGenerale('engageOuDegage', carteId);
    getMessage();
  } else {
    afficheOptionsCarte(zoneNom, carteNom, carteId);
  }
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
        /*alert(this.data.type);
        if (this.data.type == 'engage') {
          alert("hop");
          ${"#carte_" + this.data.carteId}.addClass("engagee")
        } else if (this.data.type == 'degage') {
          ${"#carte_" + this.data.carteId}.removeClass("engagee")
        } */
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
