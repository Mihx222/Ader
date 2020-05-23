import categoryEndpoint from "./endpoints/category-endpoint"
import advertisementFormatEndpoint from "./endpoints/advertisement-format-endpoint";

/* global SBA */
// tag::customization-ui-endpoint[]
SBA.use({
    install({viewRegistry}) {
        viewRegistry.addView({
            name: 'instances/category',
            parent: 'instances', // <1>
            path: 'category',
            component: categoryEndpoint,
            group: "Controls",
            label: 'Categories',
            order: 1000,
            isEnabled: ({instance}) => instance.hasEndpoint('category') // <3>
        });
    }
});

SBA.use({
    install({viewRegistry}) {
        viewRegistry.addView({
            name: 'instances/formats',
            parent: 'instances', // <1>
            path: 'formats',
            component: advertisementFormatEndpoint,
            label: 'Advertisement formats',
            group: "Controls",
            order: 1000,
            isEnabled: ({instance}) => instance.hasEndpoint('format') // <3>
        });
    }
});
// end::customization-ui-endpoint[]
