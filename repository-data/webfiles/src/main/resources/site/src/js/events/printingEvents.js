export function printingEvents() {
    document.addEventListener('DOMContentLoaded', function (event) {
        window.onbeforeprint = (event) => {
            const svgNodes = document.getElementsByClassName("highcharts-root");
            console.log(svgNodes)
            Array.from(svgNodes).forEach(function(svgNode){
                console.log(svgNode)
                svgNode.remove();
            })

            const svgImages = document.querySelectorAll("#chartPrintFriendly");
            console.log(svgImages)
            Array.from(svgImages).forEach(function(svgImage){
                console.log(svgImage)
                svgImage.style.display = "block";
            })
        };

         window.onafterprint = (event) => {
             location.reload();
         };
    });
}
