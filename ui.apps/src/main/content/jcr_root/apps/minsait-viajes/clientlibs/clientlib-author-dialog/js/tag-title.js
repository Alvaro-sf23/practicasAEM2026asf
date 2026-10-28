
(function () {
    "use strict";

    function removeTagNamespace() {
        var tagFields = document.querySelectorAll(
            'foundation-autocomplete.cq-ui-tagfield[name="./cq:tags"]'
        );

        tagFields.forEach(function (field) {
            var labels = field.querySelectorAll("coral-tag-label");

            labels.forEach(function (label) {
                var text = label.textContent.trim();
                var separator = text.indexOf(":");

                if (separator !== -1) {
                    label.textContent = text.substring(separator + 1).trim();
                }
            });
        });
    }

    function init() {
        removeTagNamespace();

        var observer = new MutationObserver(function () {
            removeTagNamespace();
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true,
            characterData: true
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();