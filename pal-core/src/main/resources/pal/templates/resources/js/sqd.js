{% verbatim %}
Raphael.fn.pointer = function(x1, y1, x2, y2) {
  var size = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / 4;
  var angle = Raphael.angle(x1, y1, x2, y2);
  var p90  = Raphael.rad(angle+90);
  var m90 = Raphael.rad(angle-90);
  return new LineBuilder(x1, y1)
    .to(x1 + (Math.cos(p90) * size), y1 + (Math.sin(p90) * size))
    .to(x2, y2)
    .to(x1 + (Math.cos(m90) * size), y1 + (Math.sin(m90) * size))
    .to(x1, y1)
    .draw(this);
};

Raphael.fn.arrow = function (x1, y1, x2, y2, arrowSize) {
  var angle = Raphael.angle(x2, y2, x1, y1);
  var size = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  var normal = Raphael.rad(angle);
  var ax = x1 + Math.cos(normal) * (size - arrowSize);
  var ay = y1 + Math.sin(normal) * (size - arrowSize);
  var p90  = Raphael.rad(angle+90);
  var m90 = Raphael.rad(angle-90);


  return new LineBuilder(x1, y1)
    .to(ax, ay)
    .to(ax + (Math.cos(p90) * arrowSize), ay + (Math.sin(p90) * arrowSize / 2))
    .to(x2, y2)
    .to(ax + (Math.cos(m90) * arrowSize), ay + (Math.sin(m90) * arrowSize / 2))
    .to(ax, ay)
    .draw(this);
}

var LineBuilder = Class.create({
  initialize: function (x, y) {
    this.result = "M"+x+" "+y;
  },

  to: function (x, y) {
    this.result += "L"+x+" "+y;
    return this;
  },

  draw: function (paper) {
    return paper.path(this.result);
  }
});

var Diagram = Class.create({
  initialize: function (options) {
    this.options = jQuery.extend(true, {
      element: '#diagram',
      communications: []
    }, options || {});

    this.entities = new EntityList();
    this.communications = new CommunicationList();

    for (var i=0;i<this.options.communications.length;i++) {
      var comm = this.options.communications[i];
      this.communications.add(comm);
      this.entities
        .add(comm.origin)
        .add(comm.destination);
    }
  },

  height: function () {
    return this.communications.ySize() + this.entities.options.entity.height * 2 + 20;
  },

  draw: function () {
    var $elem = jQuery(this.options.element);
    if ($elem.length <= 0) throw "Unable to find element '"+this.options.element+"'";
    var paper = Raphael($elem[0], $elem.width(), this.height());
    var yOffset = this.entities.draw(paper, 10, 10, 50 + this.communications.ySize());

    this.communications.draw(paper, this.entities, yOffset + 50);
  }
});

var EntityList = Class.create({
  initialize: function (options) {
    this.options = jQuery.extend(true, {
      spacing: 100,
      entity: {
        width: 100,
        height: 40
      }
    }, options || {});

    this.entities = [];
    this.entityName = {};
  },
  add: function (name) {
    if (!this.entityName[name]) {
      this.entityName[name] = new Entity({
        name: name,
        size: {
          width: this.options.entity.width,
          height: this.options.entity.height
        }
      });
      this.entities.push(this.entityName[name]);
    }
    return this;
  },

  ySize: function () {
      return this.options.entity.height;
  },

  centerOf: function (name) {
    return this.entityName[name].line;
  },

  draw: function (paper, x, y1, y2) {
    var xOffset = 0;
    for (var i=0;i<this.entities.length;i++) {
      var res = this.entities[i].draw(paper, x + xOffset, y1);
      this.entities[i].draw(paper, x + xOffset, y2);
      new LineBuilder(this.entities[i].line, y1+this.options.entity.height)
        .to(this.entities[i].line, y2)
        .draw(paper).node.setAttribute('class', 'sqd-entity-line');
      xOffset += res.width + this.options.spacing;
    }
    return this.options.entity.height;
  }
});

var CommunicationList = Class.create({
  initialize: function (options) {
    this.options = jQuery.extend(true, {
      spacing: 50,
      bottom: 20
    }, options || {});

    this.communications = [];
  },

  add: function (communication) {
    this.communications.push(new Communication(communication));
    return this;
  },

  ySize: function () {
    return this.communications.length * (this.options.spacing + 1) + this.options.bottom;
  },

  draw: function (paper, entities, y) {
    for (var i = 0;i<this.communications.length;i++) {
      var comm = this.communications[i];
      comm.draw(paper, y + (this.options.spacing * i),
        entities.centerOf(comm.origin),
        entities.centerOf(comm.destination)
      );
    }
  }
});


var Communication = Class.create({
  initialize: function (options) {
      this.options = jQuery.extend(true, {
        origin: 'Unknown',
        destination: 'Unknown',
        message: { id: 'unique-id', subject: 'Unknown', html: '<b>No Content</b>' }
      }, options || {});

      this.origin = this.options.origin;
      this.destination = this.options.destination;
  },

  draw: function (paper, y, x1, x2) {
    var middle = x1 + ((x2 - x1) / 2);
    if (x2 < x1)
       middle = x2 + (x1 - x2) / 2;
    var textNode = paper.text(middle, y - 10, this.options.message.subject).node;
    var arrowNode = paper.arrow(x1, y, x2, y, 10).node;
    textNode.setAttribute('class', 'sqd-subject');
    arrowNode.setAttribute('class', 'sqd-arrow');
    var $elem = jQuery([textNode, arrowNode]);
    $elem.attr('data-id', this.options.message.id);
    $elem.data('html', this.options.message.html);
    $elem.data('subject', this.options.message.subject);
    $elem.click(function () {
      jQuery('#content-modal div[data-selector="body"]').html(jQuery(this).data('html'));
      jQuery('#content-modal *[data-selector="title"]').html(jQuery(this).data('subject'));
      jQuery('#content-modal').modal({
        backdrop: false
      });
    });
  }
});


var Entity = Class.create({
  initialize: function (options) {
      this.options = jQuery.extend(true, {
        name: 'Unknown',
        size: {
          width: 100,
          height: 40
        }
      }, options || {});
  },


  getName: function () {
    return this.options.name;
  },

  draw: function (paper, xOffset, yOffset) {
    paper.rect(xOffset, yOffset, this.options.size.width, this.options.size.height)
      .node.setAttribute('class', 'sqd-entity-box');
    paper.text(xOffset + (this.options.size.width / 2), yOffset + (this.options.size.height / 2), this.options.name)
      .node.setAttribute('class', 'sqd-entity-name');
    this.line = xOffset + (this.options.size.width / 2);

    return {
      height: this.options.size.height,
      width: this.options.size.width
    };
  }
});
{% endverbatim %}