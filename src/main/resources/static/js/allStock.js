//    function getAllStock() {
//        const stock = {
//            stockId: '',
//            stockName: '',
//            stockPrice: 0,
//            changes: '',
//            rate: '',
//            marketCode: ''
//         } ;
//
//
//      $.ajax({
//          url: '/volume_rank'
//          , method: 'GET'
//          , async: false   // false: 비동기
//    //      , data: { idx : idx }
//    //      { stock.stockId: srtnCd, stock.itmsNm: itmsNm }
//          , success: function(data) {
//            stock.stockId = data.srtnCd;
//            stock.stockName = data.itmsNm;
//            stock.stockPrice = data.clpr;
//            stock.changes = data.vs;
//            stock.rate = data.fltRt;
//            stock.marketCode = data.mrktCtg;
//
//           console.log(data);
//          }
//       });
//
//       $('#stockId').text(stock.stockId);
//              $('#stockName').text(stock.stockName);
//                $('#stockPrice').text(stock.stockPrice);
//                $('#changes').text(stock.changes);
//                 $('#rate').text(stock.rate);
//                 $('#marketCode').text(stock.marketCode);
//    }
//
//getAllStock();
//
//
//
//function getAllStock() {
//    const stock = {
//        stockId: '',
//        stockName: '',
//        stockPrice: 0,
//        changes: '',
//        rate: '',
//        marketCode: ''
//    };
//
//    $.ajax({
//        url: '/all_stock',
//        method: 'GET',
//        async: false,
//        success: function (data) {
//            stock.stockId = data.stockId;
//            stock.stockName = data.itmsNm;
//            stock.stockPrice = data.clpr;
//            stock.changes = data.vs;
//            stock.rate = data.fltRt;
//            stock.marketCode = data.mrktCtg;
//
//            // 주식 데이터를 HTML에 표시
//            $('#stockInfoContainer').html(`
//                <div class="stock">
//                    <h2>주식 정보</h2>
//                    <p><strong>종목 코드:</strong> ${stock.stockId}</p>
//                    <p><strong>종목 이름:</strong> ${stock.stockName}</p>
//                    <p><strong>현재 가격:</strong> ${stock.stockPric    e} 원</p>
//                    <p><strong>전일 대비:</strong> ${stock.changes}</p>
//                    <p><strong>등락률:</strong> ${stock.rate}%</p>
//                    <p><strong>시장 코드:</strong> ${stock.marketCode}</p>
//                </div>
//            `);
//        }
//    });
//}
//
//// 페이지가 로드될 때 주식 데이터를 가져와서 화면에 출력
//$(document).ready(function() {
//    getAllStock();
//});

function getAllStock() {
    // 주식 정보를 담을 배열
    const stocks = [];

    $.ajax({
        url: '/all_stock',  // 주식 정보를 가져올 URL
        method: 'GET',
        async: false, // 비동기 요청
        success: function(data) {
        console.log(data)
            // data가 배열이라 가정하고, 처음 10개만 선택
            const stockData = data.slice(0, 10); // data에서 첫 10개 항목 선택

            // 각 주식 정보를 stocks 배열에 추가
            stockData.forEach(stock => {
                stocks.push({
                    stockId: stock.srtnCd,
                    stockName: stock.itmsNm,
                    stockPrice: stock.clpr,
                    changes: stock.vs,
                    rate: stock.fltRt,
                    marketCode: stock.mrktCtg
                });
            });

            // 주식 정보를 HTML에 표시
            let stockInfoHtml = '<div class="stocks">'; // 여러 주식 정보를 담을 div

            stocks.forEach(stock => {
                stockInfoHtml += `
                    <div class="stock">
                        <h2>주식 정보</h2>
                        <p><strong>종목 코드:</strong> ${stock.stockId}</p>
                        <p><strong>종목 이름:</strong> ${stock.stockName}</p>
                        <p><strong>현재 가격:</strong> ${stock.stockPrice} 원</p>
                        <p><strong>전일 대비:</strong> ${stock.changes}</p>
                        <p><strong>등락률:</strong> ${stock.rate}%</p>
                        <p><strong>시장 코드:</strong> ${stock.marketCode}</p>
                        <hr />  <!-- 구분선 추가 -->
                    </div>
                `;
            });

            stockInfoHtml += '</div>'; // 여러 주식 정보를 담은 div 종료

            $('#stockInfoContainer').html(stockInfoHtml); // 결과를 페이지에 표시
        }
    });
}

getAllStock();

// 페이지가 로드될 때 주식 데이터를 가져와서 화면에 출력
$(document).ready(function() {
    getAllStock();
});


//function getAllStock() {
//
//    $.ajax({
//        url: '/all_stock',  // 주식 정보를 가져올 URL
//        method: 'GET',
//        async: false, // 비동기 요청
//        success: function(data) {
//        console.log(data)
//        }
//    });
//}
//
//getAllStock();
//
//// 페이지가 로드될 때 주식 데이터를 가져와서 화면에 출력
//$(document).ready(function() {
//    $.getJSON("all_stock", function(data) {
//        let stock_data = "";
//
//        $.each(data.)
//    }
//
//});
