export function printingEvents() {
    document.addEventListener('DOMContentLoaded', function (event) {
        window.onbeforeprint = (event) => {
            const svgNode = document.getElementsByClassName("highcharts-root");
            let i;
            for (i = 0; i < svgNode.length; i++) {
                console.log(svgNode[i])
                svgNode[i].remove();
            }
            const svgImage = document.getElementById("chartPrintFriendly");
            svgImage.style.display = "block";
        };

         window.onafterprint = (event) => {
             location.reload();
         };
    });
}
