var ChartColor = [
  "#5D62B4",
  "#54C3BE",
  "#EF726F",
  "#F9C446",
  "rgb(93.0, 98.0, 180.0)",
  "#21B7EC",
  "#04BCCC",
];
var primaryColor = getComputedStyle(document.body).getPropertyValue(
  "--primary"
);
var secondaryColor = getComputedStyle(document.body).getPropertyValue(
  "--secondary"
);
var successColor = getComputedStyle(document.body).getPropertyValue(
  "--success"
);
var warningColor = getComputedStyle(document.body).getPropertyValue(
  "--warning"
);
var dangerColor = getComputedStyle(document.body).getPropertyValue("--danger");
var infoColor = getComputedStyle(document.body).getPropertyValue("--info");
var darkColor = getComputedStyle(document.body).getPropertyValue("--dark");
var lightColor = getComputedStyle(document.body).getPropertyValue("--light");

(function ($) {
  "use strict";
  $(function () {
    var body = $("body");
    var sidebar = $(".sidebar");
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    //Add active class to nav-link based on url dynamically
    //Active class can be hard coded directly in html file also as required

    function addActiveClass(element) {
      // check if element is dashboard
      var elementUrl = element[0].href.split("/");
      if (!queryString && elementUrl[elementUrl.length - 1] === "") {
        element.parents(".nav-item").last().addClass("active");
        return;
      }

      if (element[0].href === window.location.href) {
        element.parents(".nav-item").last().addClass("active");
        if (element.parents(".sub-menu").length) {
          element.closest(".collapse").addClass("show");
          element.addClass("active");
        }
        if (element.parents(".submenu-item").length) {
          element.addClass("active");
        }
      } else if (urlParams.get("act")) {
        const elementAct = element[0].href.split("?")[1] ?? "";
        if (elementAct) {
          const paramElementAct = elementAct.split("&");
          if (paramElementAct.length == 1) {
            paramElementAct.forEach((param) => {
              if (param.includes(urlParams.get("page"))) {
                element.parents(".nav-item").last().addClass("active");
                if (element.parents(".sub-menu").length) {
                  element.closest(".collapse").addClass("show");
                }
              }
            });
          }
        }
      }
    }

    $(".nav li a", sidebar).each(function () {
      var $this = $(this);
      addActiveClass($this);
    });

    //Close other submenu in sidebar on opening any

    sidebar.on("show.bs.collapse", ".collapse", function () {
      sidebar.find(".collapse.show").collapse("hide");
    });

    //Change sidebar and content-wrapper height
    applyStyles();

    function applyStyles() {
      //Applying perfect scrollbar
      if (!body.hasClass("rtl")) {
        if (body.hasClass("sidebar-fixed")) {
          var fixedSidebarScroll = new PerfectScrollbar("#sidebar .nav");
        }
      }
    }

    $('[data-toggle="minimize"]').on("click", function () {
      if (
        body.hasClass("sidebar-toggle-display") ||
        body.hasClass("sidebar-absolute")
      ) {
        body.toggleClass("sidebar-hidden");
      } else {
        body.toggleClass("sidebar-icon-only");
      }
    });

    //checkbox and radios
    $(".form-check label,.form-radio label").append(
      '<i class="input-helper"></i>'
    );

    if ($(".navbar").hasClass("fixed-top")) {
      document.querySelector(".page-body-wrapper").classList.remove("pt-0");
      document.querySelector(".navbar").classList.remove("pt-5");
    } else {
      document.querySelector(".page-body-wrapper").classList.add("pt-0");
      document.querySelector(".navbar").classList.add("pt-5");
      document.querySelector(".navbar").classList.add("mt-3");
    }
  });
})(jQuery);
