// Basic dates in dd/mm/yy or dd-mm-yy format.
// Years can be 4 digits. Days and Months can be 1 or 2 digits.
import Tablesort from "tablesort";

export function tableSortDate() {
    const parseDate = function (date) {
        const parsedDate = date.replace(/-/g, '/')
            .replace(/(\d{1,2})[/-](\d{1,2})[/-](\d{2,4})/, '$3-$2-$1'); // format before getTime

        return new Date(parsedDate).getTime() || -1;
    };

    Tablesort.extend('date', function (item) {
        return (
            item.search(/(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\.?,?\s*/i) !== -1 ||
            item.search(/\d{1,2}[/-]\d{1,2}[/-]\d{2,4}/) !== -1 ||
            item.search(/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)/i) !== -1
        ) && !Number.isNaN(parseDate(item));
    }, function (a, b) {
        const aLower = a.toLowerCase();
        const bLower = b.toLowerCase();

        return parseDate(bLower) - parseDate(aLower);
    });
};
