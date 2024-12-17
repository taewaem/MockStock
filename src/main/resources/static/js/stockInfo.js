//$(document).ready(function() {
//    getStockInfo();
//
//
//});
//
//function getStockInfo() {
//    $.ajax({
//        url: '/stock_info',
//        method: 'GET',
//        async: true,  // 비동기로 처리 (true)
//        success: function(data) {
//            console.log(data);  // 데이터를 확인하기 위해 로그 출력
//            if (data.rt_cd === '0') {
//                let stock_info_data = "";
//                let output = data.output;
//
//                stock_info_data += "<tr>";
//                stock_info_data += "<td>" + output.std_idst_clsf_cd_name + "</td>";  // 산업분류
//                stock_info_data += "<td>" + output.idx_bztp_mcls_cd_name + "</td>";  // 업종
//                stock_info_data += "<td>" + output.pdno.substring(output.pdno.length-6) + "</td>";  // 주식 코드
//                stock_info_data += "</tr>";
//
//                $("#stock_info_table tbody").append(stock_info_data);  // tbody에 추가
//
//                 $('#buyBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
//                 $('#sellBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
//            }
//            else{
//            console.log("정보를 불러오지 못했습니다")
//            }
//        }
//    });
//}
////
////data: {
////            stockId:
////        }


//$(document).ready(function() {
//
//    // stock-item 클릭 이벤트
//    document.querySelectorAll('.stock-item').forEach(item => {
//        item.addEventListener('click', function() {
//            const stockId = this.getAttribute('data-stock-id');  // 클릭된 주식 ID 가져오기
//            console.log("Clicked stockId:", stockId);
//
//            const form = document.createElement('form');
//            form.method = 'POST';
//            form.action = '/stockInfo/' + stockId;
//
//            const input = document.createElement('input');
//            input.type = 'hidden';
//            input.name = 'stockId';
//            input.value = stockId;
//            form.appendChild(input);
//
//            document.body.appendChild(form);
//            form.submit();
//
//            // 주식 정보 가져오기 (stockId 전달)
//            getStockInfo(stockId);
//        });
//    });
//
//    // 매수 버튼 클릭 이벤트
//    $('#buyBtn').click(function() {
//        var stockId = $(this).data('stock-id'); // 주식 ID 가져오기
//        console.log("Buy stockId:", stockId);
//        window.location.href = '/buyStock/' + stockId; // 주식 ID 사용하여 페이지 이동
//    });
//
//    // 매도 버튼 클릭 이벤트
//    $('#sellBtn').click(function() {
//        var stockId = $(this).data('stock-id'); // 주식 ID 가져오기
//        console.log("Sell stockId:", stockId);
//        window.location.href = '/sellStock/' + stockId; // 주식 ID 사용하여 페이지 이동
//    });
//});
//
//// 주식 정보 가져오기 (stockId를 인자로 받음)
//function getStockInfo(stockId) {
//    $.ajax({
//        url: '/stockInfoApi',
//        method: 'GET',
//        async: true,
//        data: {
//            stockId: stockId  // 주식 ID 전달
//        },
//        success: function(data) {
//            console.log(data);
//            if (data.rt_cd === '0') {
//                let stock_info_data = "";
//                let output = data.output;
//
//                stock_info_data += "<tr>";
//                stock_info_data += "<td>" + output.std_idst_clsf_cd_name + "</td>";  // 산업 분류
//                stock_info_data += "<td>" + output.idx_bztp_mcls_cd_name + "</td>";  // 업종
//                stock_info_data += "<td>" + output.pdno.substring(output.pdno.length-6) + "</td>";  // 주식 코드
//                stock_info_data += "</tr>";
//
//                // 주식 정보를 테이블에 추가
//                $("#stock_info_table tbody").append(stock_info_data);
//
//                // 매수, 매도 버튼에 주식 ID 설정
//                $('#buyBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
//                $('#sellBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
//            } else {
//                console.log("정보를 불러오지 못했습니다");
//            }
//        },
//        error: function() {
//            console.error("AJAX 요청 실패:");
//        }
//    });
//}


$(document).ready(function() {
    // stock-item 클릭 이벤트
    document.querySelectorAll('.stock-item').forEach(item => {
        item.addEventListener('click', function() {
            const stockId = this.getAttribute('data-stock-id');  // 클릭된 주식 ID 가져오기
            console.log("Clicked stockId:", stockId);

            window.location.href = '/stockInfo/' + stockId; // 해당 주식 정보 페이지로 이동

            // 주식 정보 가져오기 (stockId 전달)
            getStockInfo(stockId);
            getPriceInfo(stockId)
        });
    });

    // 매수 버튼 클릭 이벤트
    $('#buyBtn').click(function() {
        var stockId = $(this).data('stock-id'); // 주식 ID 가져오기
        console.log("Buy stockId:", stockId);
        window.location.href = '/buyStock/' + stockId; // 주식 ID 사용하여 페이지 이동
    });

    // 매도 버튼 클릭 이벤트
    $('#sellBtn').click(function() {
        var stockId = $(this).data('stock-id'); // 주식 ID 가져오기
        console.log("Sell stockId:", stockId);
        window.location.href = '/sellStock/' + stockId; // 주식 ID 사용하여 페이지 이동
    });
});

// 주식 정보 가져오기 (stockId를 인자로 받음)
function getStockInfo(stockId) {
    $.ajax({
        url: '/stockInfoApi',
        method: 'GET',
        async: false,
        data: {
            stockId: stockId  // 주식 ID 전달
        },
        success: function(data) {
            console.log(data);
                let stock_info_data = "";
                let output = data.output;

                stock_info_data += "<tr>";
                stock_info_data += "<td>" + output.std_idst_clsf_cd_name + "</td>";  // 산업 분류
                stock_info_data += "<td>" + output.idx_bztp_mcls_cd_name + "</td>";  // 업종
                stock_info_data += "<td>" + output.pdno.substring(output.pdno.length-6) + "</td>";  // 주식 코드
                stock_info_data += "</tr>";

                // 주식 정보를 테이블에 추가
                $("#stock_info_table tbody").empty().append(stock_info_data);  // 기존 내용 삭제 후 추가

                // 매수, 매도 버튼에 주식 ID 설정
                $('#buyBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
                $('#sellBtn').data('stock-id', output.pdno.substring(output.pdno.length - 6));
        },
        error: function() {
            console.error("AJAX 요청 실패:");
        }
    });
}

function getPriceInfo(stockId) {
    $.ajax({
        url: '/curPriceApi',
        method: 'GET',
        async: false,
        data: {
            stockId: stockId  // 주식 ID 전달
        },
        success: function(data) {
            console.log(data);
                let price_info_data = "";
                let output = data.output;

                price_info_data += "<tr>";
                price_info_data += "<td>" + output.acml_vol + "</td>";  // 누적 거래량
                price_info_data += "<td>" + output.hts_avls + "</td>";  // 시가총액
                price_info_data += "<td>" + output.w52_hgpr + "</td>";  // 52주 최고가
                price_info_data += "<td>" + output.w52_lwpr + "</td>";  // 52주 최저가
                price_info_data += "<td>" + output.per + "</td>";       // PER
                price_info_data += "<td>" + output.pbr + "</td>";       // PBR
                price_info_data += "<td>" + output.cpfn + "</td>";      // 자본금
                price_info_data += "</tr>";

                // 주식 정보를 테이블에 추가
                $("#price_info_table tbody").empty().append(price_info_data);  // 기존 내용 삭제 후 추가
        },
        error: function() {
            console.error("AJAX 요청 실패:");
        }
    });
}
