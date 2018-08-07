goog.provide("tiny.react_checks");

tiny.react_checks.add_dev_props = function add_dev_props(element, self, source) {
  element._store = {};

  Object.defineProperty(element._store, 'validated', {
    configurable: false,
    enumerable: false,
    writable: true,
    value: false,
  });

  Object.defineProperty(element, '_self', {
    configurable: false,
    enumerable: false,
    writable: false,
    value: self,
  });

  Object.defineProperty(element, '_source', {
    configurable: false,
    enumerable: false,
    writable: false,
    value: source,
  });

  if (Object.freeze) {
    Object.freeze(element.props);
    Object.freeze(element);
  }

  return element;
};
