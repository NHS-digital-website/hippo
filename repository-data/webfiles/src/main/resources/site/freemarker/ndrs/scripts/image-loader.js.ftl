<#ftl output_format="HTML">

<script>
    /* eslint-disable no-underscore-dangle */
    window.ImageLoader = function () {
        const _this = this;
        _this.originalQueue = [];
        _this.queue = [];
        _this.loaded = 0;
        _this.total = 0;

        function _handleImageLoadComplete() {
            _this.loaded += 1;
            $(_this).trigger('progress', _this.loaded / _this.total);

            if (_this.loaded === _this.total) {
                // console.log('[imageLoader] - _handleImageLoadComplete(): DONE');
                $(_this).trigger('completed');
            }
        }
        function _load(source) {
            const img = new Image();
            img.onload = function () {
                _handleImageLoadComplete();
            }
            img.onerror = function () {
                console.warn('[ImageLoader] - onerror: There was an error loading the image: ', img.src);
                _handleImageLoadComplete();
            }
            img.src = source;
        }
        function _addToQueue(sources) {
            _this.originalQueue = [];
            _this.queue = [];
            for (let i = 0; i < sources.length; i++) {
                _this.queue.push(sources[i]);
            }
            _this.originalQueue = _this.queue;
            _this.total = _this.queue.length;
        }
        function _loadQueue() {
            for (let i = 0; i < _this.queue.length; i++) {
                _load(_this.queue[i]);
            }
        }
        function _reset() {
            _this.originalQueue = [];
            _this.queue = [];
            _this.total = 0;
            _this.loaded = 0;
        }
        _this.addToQueue = _addToQueue;
        _this.loadQueue = _loadQueue;
        _this.reset = _reset;
        return _this;
    }
</script>
