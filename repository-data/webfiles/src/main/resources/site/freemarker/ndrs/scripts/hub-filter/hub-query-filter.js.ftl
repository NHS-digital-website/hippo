<#ftl output_format="HTML">

<script>
    function HubQueryFilter($el) {
        if (typeof $el === 'undefined') {
            return;
        }

        var DEFAULT_VALUE = 'DEFAULT_VALUE';
        var DEFAULT_KEY = 'DEFAULT_KEY';
        var TYPE = 'QUERY';

        var _this = this;
        _this.$el = $el;

        _this.$formEl = _this.$el.querySelector('form');
        _this.$inputEl = _this.$el.querySelector('input[type="text"]');
        _this.$buttonEl = _this.$el.querySelector('button');
        _this.key = _this.$el.getAttribute('data-hub-filter-key') || DEFAULT_KEY;
        setValue(DEFAULT_VALUE);
        _this.name = generateName();

        function getType() {
            return TYPE;
        }

        function getKey() {
            return _this.key;
        }

        function resetValue(value) {
            value = typeof value !== 'undefined' ? value : '';
            setValue(value);
            _this.$inputEl.value = value;
        }

        function setValue(value) {
            _this.value = value;
        }

        function getValue() {
            return _this.value;
        }

        function getDefaultValue() {
            if (getValue() === DEFAULT_VALUE) {
                return;
            }

            return getValue();
        }

        function getEl() {
            return _this.$el;
        }

        function getName() {
            return _this.name;
        }

        function generateName() {
            return 'hubQueryFilter_' + Math.round(Math.random() * 1000000) + '-' + Math.round(Math.random() * 1000000);
        }

        function onChange(callback) {
            if (typeof callback === 'function') {
                _this.onChangeCallback = callback;
            }
        }

        function showProgress(complete) {
            if (complete) {
                window.vjsu.removeClass(_this.$formEl, 'is-loading');
            } else {
                window.vjsu.addClass(_this.$formEl, 'is-loading');
            }
        }

        function handleValueUpdate() {
            setValue(_this.$inputEl.value);

            if (typeof _this.onChangeCallback === 'function') {
                _this.onChangeCallback.apply(this, [hubQueryFilter]);
            }
        }

        (function init() {
            if (_this.$inputEl.value.length) {
                setValue(_this.$inputEl.value);
            }

            _this.$inputEl.addEventListener('keyup', function() {
                handleValueUpdate();
            });

            _this.$buttonEl.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();

                handleValueUpdate();
            });
        })();

        var hubQueryFilter =  {
            getKey:             getKey,
            getValue:           getValue,
            resetValue:         resetValue,
            getDefaultValue:    getDefaultValue,
            getName:            getName,
            getEl:              getEl,
            onChange:           onChange,
            showProgress:       showProgress,
            getType:            getType
        };

        return hubQueryFilter;
    };
</script>
