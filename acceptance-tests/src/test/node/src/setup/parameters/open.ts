import {defineParameterType} from "@cucumber/cucumber";

defineParameterType({
    name: 'open',
    regexp: /"(open|closed)"/,
    transformer: function(stringValue) {
        return stringValue == "open";
    },
})
