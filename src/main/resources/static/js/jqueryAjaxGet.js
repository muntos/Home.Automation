$(document).ready(function() {

    ajaxGet();

    // DO GET
    function ajaxGet(){
        $.ajax({
            type : "GET",
            url : "/home/info/log",
            success: function(result){
                $.each(result, function(i, log){

                    var logRow = '<tr>' +
                        '<td>' + log.date + '</td>' +
                        '<td>' +log.level + '</td>' +
                        '<td>' + log.message + '</td>' +
                        '</tr>';

                    $('#logTable tbody').append(logRow);

                });

                $( "#logTable tbody tr:odd" ).addClass("info");
                $( "#logTable tbody tr:even" ).addClass("success");
            },
            error : function(e) {
                alert("ERROR: ", e);
                console.log("ERROR: ", e);
            }
        });
    }
})