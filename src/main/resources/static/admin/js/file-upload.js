(function ($) {
  "use strict";
  $(function () {
    $(".file-upload-browse").on("click", function () {
      var fileParent = $(this).parent().parent().parent();
      var file = fileParent.find(".file-upload-default");
      file.trigger("click");
    });
    $(".file-upload-default").on("change", function () {
      $(this)
        .parent()
        .find(".form-control")
        .val(
          $(this)
            .val()
            .replace(/C:\\fakepath\\/i, "")
        );
      var fileInput = $(this)[0];
      var fileParent = $(this).parent().parent();
      var previewImage = fileParent.find("#previewImage");
      var imgPreview = fileParent.find(".img-preview");

      if (fileInput.files && fileInput.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
          previewImage.attr("src", e.target.result);
          imgPreview.removeClass("img-preview");
        };

        reader.readAsDataURL(fileInput.files[0]);
      }
    });
    $("#deleteImage").on("click", function () {
      var fileParent = $(this).parent();
      var file = fileParent.find(".file-upload-default");
      var previewImage = fileParent.find("#previewImage");
      var imgPreview = previewImage.parent();

      file.val("");
      previewImage.attr("src", null);
      imgPreview.addClass("img-preview");
    });
  });
})(jQuery);
