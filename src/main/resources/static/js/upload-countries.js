// upload-countries.js
// Popola la <select id="country"> con nomi dei Paesi localizzati e valori ISO.
// La lingua dei nomi si adatta al browser, mentre il valore passato al server è il codice ISO.

document.addEventListener('DOMContentLoaded', () => {
    const select = document.getElementById('country');
    if (!select) {
        console.warn('⚠️ upload-countries.js: <select id="country"> not found.');
        return;
    }

    const userLocale = navigator.language || 'en-US';
    const [browserLang, browserRegion] = userLocale.split('-');
    const supportedLangs = ['en', 'it', 'fr', 'de', 'es', 'pt', 'nl', 'sv', 'pl', 'ru', 'ja', 'zh', 'ar'];
    const lang = supportedLangs.includes(browserLang) ? browserLang : 'en';
    const storageKey = `countries_${lang}`;

    // Usa cache se disponibile
    const cached = sessionStorage.getItem(storageKey);
    if (cached) {
        const countries = JSON.parse(cached);
        populateSelect(select, countries, browserRegion);
        return;
    }

    // Lista ISO dei Paesi
    const isoCountries = [
        'AF','AL','DZ','AS','AD','AO','AI','AQ','AG','AR','AM','AW','AU','AT','AZ',
        'BS','BH','BD','BB','BY','BE','BZ','BJ','BM','BT','BO','BA','BW','BR','IO',
        'BN','BG','BF','BI','CV','KH','CM','CA','KY','CF','TD','CL','CN','CO','KM',
        'CG','CD','CR','CI','HR','CU','CY','CZ','DK','DJ','DM','DO','EC','EG','SV',
        'GQ','ER','EE','SZ','ET','FJ','FI','FR','GA','GM','GE','DE','GH','GR','GD',
        'GU','GT','GN','GW','GY','HT','HN','HK','HU','IS','IN','ID','IR','IQ','IE',
        'IL','IT','JM','JP','JO','KZ','KE','KI','KW','KG','LA','LV','LB','LS','LR',
        'LY','LI','LT','LU','MO','MG','MW','MY','MV','ML','MT','MH','MR','MU','MX',
        'FM','MD','MC','MN','ME','MA','MZ','MM','NA','NR','NP','NL','NZ','NI','NE',
        'NG','KP','MK','NO','OM','PK','PW','PS','PA','PG','PY','PE','PH','PL','PT',
        'PR','QA','RO','RU','RW','KN','LC','VC','WS','SM','ST','SA','SN','RS','SC',
        'SL','SG','SK','SI','SB','SO','ZA','KR','SS','ES','LK','SD','SR','SE','CH',
        'SY','TW','TJ','TZ','TH','TL','TG','TO','TT','TN','TR','TM','TV','UG','UA',
        'AE','GB','US','UY','UZ','VU','VA','VE','VN','YE','ZM','ZW'
    ];

    let regionNames;
    try {
        regionNames = new Intl.DisplayNames([lang], { type: 'region' });
    } catch (e) {
        console.warn(`⚠️ Language "${lang}" not supported. Fallback to English. Error:`, e);
        regionNames = new Intl.DisplayNames(['en'], { type: 'region' });
    }

    const countries = isoCountries
        .map(code => ({ code, name: regionNames.of(code) }))
        .filter(c => c.name)
        .sort((a, b) => a.name.localeCompare(b.name));

    sessionStorage.setItem(storageKey, JSON.stringify(countries));

    populateSelect(select, countries, browserRegion);
});

function populateSelect(select, countries, browserRegion) {
    // Remove all options except the placeholder
    for (const opt of select.querySelectorAll('option:not(:first-child)')) {
        opt.remove();
    }

    let autoSelectValue = null;

    for (const country of countries) {
        const option = document.createElement('option');
        option.value = country.code;      // ✅ ISO value for server
        option.textContent = country.name; // Localized name for user

        if (browserRegion && country.code === browserRegion.toUpperCase()) {
            autoSelectValue = country.code;
        }

        select.appendChild(option);
    }

    if (autoSelectValue) select.value = autoSelectValue;
}


