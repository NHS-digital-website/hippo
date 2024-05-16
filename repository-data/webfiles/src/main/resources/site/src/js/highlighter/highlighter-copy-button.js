import { qsa } from "../utils/utils";

function activateCodeBlock(element) {
    const codeBlock = element.querySelector('.button-code-block');
    if (!navigator.clipboard) {
        // No browser support.
        codeBlock.parentElement.removeChild(codeBlock);
    } else {
        codeBlock.addEventListener('click', function (event) {
            const btn = event.target;
            const codeBlockText = codeBlock.parentElement.parentElement.querySelector('pre').innerText;
            navigator.clipboard.writeText(codeBlockText).then(function() {
                btn.classList.add("button-code-block-done");
                setTimeout(function() {
                    btn.classList.remove("button-code-block-done");
                }, 1200)
            }, function() {
                btn.innerText(' Could not copy code snip-it! ');
            });
        });
    }
}

export function activateCodeBlocks() {
    qsa('.code-block').forEach(activateCodeBlock)
}
