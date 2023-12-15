$(function () {
    //搜索框验证
    $('form').submit(function () {
        if ($(this).find("input[name='product_name']").val() === "") {
            alert("请输入关键字！");
            return false;
        }
    });
    $(".tbody_checkbox>.cbx_select").click(function () {
        const obj = $(this).parents("tr.orderItem_info").toggleClass("orderItem_selected");
        sumPrice();
    });
    $("#cbx_select_all,#J_SelectAllCbx2").click(function () {
        const yn = $(this).prop("checked");
        const obj = $("tr.orderItem_info");
        if (!yn) {
            $(".tbody_checkbox>.cbx_select").prop("checked", false);
            sumPrice();
            obj.removeClass("orderItem_selected");
            $("#J_Go").removeClass("selected");
        } else {
            $(".tbody_checkbox>.cbx_select").prop("checked", true);
            sumPrice();
            obj.addClass("orderItem_selected");
            $("#J_Go").addClass("selected");
        }
    });
    $(".item_amount>input[type=text]").bind('input propertychange', function () {
        let number = $(this).val();
        if (isNaN(number) || $.trim(number) === "") {
            $(this).val(1);
            $(this).prev("a").addClass("no_minus");
            return;
        }
        if (parseInt(number) >= 500) {
            $(this).val(500);
            number = 500;
            $(this).next("a").addClass("no_minus");
        } else if (parseInt(number) > 1 && parseInt(number) < 500) {
            $(this).prev("a").removeClass("no_minus");
            $(this).next("a").removeClass("no_minus");
        } else if (parseInt(number) <= 1) {
            $(this).val(1);
            number = 1;
            $(this).prev("a").addClass("no_minus");
        }
        let price = $(this).parents("tr").find(".orderItem_product_price").text();
        price = parseFloat(price.substring(1));
        const price_sum = parseFloat(price * parseInt(number));
        $(this).parents("tr").find(".orderItem_product_realPrice").text("￥" + price_sum.toFixed(1));
        sumPrice();
    });
});

function up(obj) {
    obj = $(obj);
    const number = obj.next("input");
    let value = parseInt(number.val());
    if (value > 1) {
        obj.removeClass("no_minus");
    } else {
        obj.addClass("no_minus");
    }
    if (obj.hasClass("no_minus")) {
        return true;
    } else {
        if (isNaN(number.val()) || $.trim(number.val()) === "" || parseInt(number.val()) <= 1) {
            number.val("1");
            obj.addClass("no_minus");
            return true;
        }
        value--;
        if (value < 500) {
            number.next("a").removeClass("no_minus");
        } else {
            number.next("a").addClass("no_minus");
        }
        let price = obj.parents("tr").find(".orderItem_product_price").text();
        price = parseFloat(price.substring(1));
        const price_sum = parseFloat(price * value);
        number.val(value);
        obj.parents("tr").find(".orderItem_product_realPrice").text("￥" + price_sum.toFixed(1));
        if (value === 1) {
            obj.addClass("no_minus");
        }
        sumPrice();
    }
}

function down(obj) {
    obj = $(obj);
    const number = obj.prev("input");
    let value = parseInt(number.val());
    if (value < 500) {
        obj.removeClass("no_minus");
    } else {
        obj.addClass("no_minus");
    }
    if (obj.hasClass("no_minus")) {
        return true;
    } else {
        if (isNaN(number.val()) || $.trim(number.val()) === "" || parseInt(number.val()) < 1) {
            number.val("1");
            return true;
        }
        obj.prevAll(".J_Minus").removeClass("no_minus");
        value++;
        let price = obj.parents("tr").find(".orderItem_product_price").text();
        price = parseFloat(price.substring(1));
        const price_sum = parseFloat(price * value);
        obj.parents("tr").find(".orderItem_product_realPrice").text("￥" + price_sum.toFixed(1));
        number.val(value);
        sumPrice();
    }
}

function sumPrice() {
    let price_sum = 0.00;
    const obj = $("input.cbx_select:checked").parents("tr.orderItem_info");
    obj.each(function () {
        price_sum += parseFloat($(this).find(".orderItem_product_realPrice").text().substring(1));
    });
    $(".total_value").text(price_sum.toFixed(2));

    if (obj.length > 0) {
        $("#J_Go").addClass("selected");
    } else {
        $("#J_Go").removeClass("selected");
    }
    $("#J_SelectedItemsCount").text(obj.length);
}

function create(obj) {
    obj = $(obj);
    if (!obj.hasClass("selected")) {
        return true;
    }
    const orderItemMap = {};
    const tr = $("input.cbx_select:checked").parents("tr.orderItem_info");
    tr.each(function () {
        const key = $(this).find(".input_orderItem").attr("name");
        orderItemMap[key] = $(this).find(".item_amount").children("input").val();
    });
    $.ajax({
        url: "/tmall/orderItem",
        type: "PUT",
        data: {
            "orderItemMap": JSON.stringify(orderItemMap)
        },
        traditional: true,
        success: function (data) {
            if (data.success) {
                location.href = "/tmall/order/create/byCart?order_item_list=" + data.orderItemIDArray;
                return true;
            } else {
                alert("购物车商品结算异常，请稍候再试！");
                location.href = "/tmall/cart";
            }
        },
        beforeSend: function () {
        },
        error: function () {
            alert("购物车商品结算异常，请稍候再试！");
            location.href = "/tmall/cart";
        }
    });
}
