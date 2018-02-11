$(document).ready(function() {

    displayLog();

    $("#clearLog").click(function(){
       clearLog();
    });

    function clearLog() {
        $.ajax({type: "DELETE",
            url: "/home/info/log",
            success:function(result){
                location.reload();
                //displayLog();
            }});
    }

    function displayLog(){
        $.ajax({
            type : "GET",
            url : "/home/info/log",
            success: function(result){
                $.each(result, function(i, log){
                    var color;
                    switch (log.level){
                        case "DEBUG":
                            color = "blue";
                            break;
                        case "WARN":
                            color = "orange";
                            break;
                        case "ERROR":
                            color = "red";
                            break;
                        default:
                            color = "black";
                    }

                    var logRow = '<tr>' +
                        '<td style="white-space: nowrap">' + log.date + '</td>' +
                        '<td style="color:' + color +'">' +log.level + '</td>' +
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