const weeks = ['日', '月', '火', '水', '木', '金', '土'];
let date = new Date(stringDate);
let year = date.getFullYear();  // 受け取った開始日から年を算出
let month = date.getMonth() + 1;  // 受け取った開始日から月を算出

function showCalendar(year, month) {
    const calendarHtml = createCalendar(year, month);
    const sec = document.createElement('section');
    sec.innerHTML = calendarHtml;
    document.querySelector('#calendar').appendChild(sec);
}

function createCalendar(year, month) {
    const startDate = new Date(year, month - 1, 1);  // 月の最初の日を取得
    const endDate = new Date(year, month, 0);  // 月の最後の日を取得
    const endDayCount = endDate.getDate();  // 月の末日
    const startDay = startDate.getDay();  // 月の最初の日の曜日を取得


    const startDateString = startDate.toISOString().split("T")[0]; // "yyyy-MM-dd" 形式に変更
    const endDateString = endDate.toISOString().split("T")[0]; // "yyyy-MM-dd" 形式に変更
    document.getElementById("previousMonth").value = startDateString;  //前月ボタンのvalueに月の初日をセット
    document.getElementById("nextMonth").value = endDateString;  //次月ボタンのvalueに月の最終日をセット

    let dayCount = 1;  // 日にちのカウント
    let calendarHtml = '';  // HTMLを組み立てる変数

    calendarHtml += `<h1>${year}/${month}</h1>`;
    calendarHtml += '<table>';

    // 曜日の行を作成
    for (let i = 0; i < weeks.length; i++) {
        calendarHtml += `<td class="dateLine">${weeks[i]}</td>`;
    }

    for (let w = 0; w < 6; w++) {
        calendarHtml += '<tr>';

        for (let d = 0; d < 7; d++) {
            if (w == 0 && d < startDay) {
                // 1行目で1日の曜日の前
                calendarHtml += '<td></td>';
            } else if (dayCount > endDayCount) {
                // 末尾の日数を超えた
                calendarHtml += '<td></td>';
                dayCount++;
            } else {
                const dayData = recordList && recordList.find(item => item.date === `${year}-${String(month).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}`);
                if(dayData != undefined){
                    let income = dayData.incomeAmount === 0 ? '' : dayData.incomeAmount;
                    let expense = dayData.expenseAmount === 0 ? '' : dayData.expenseAmount;
                    calendarHtml +=
                        `<td class="calendar_td" data-date="${year}-${String(month).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}">
                            <p class="day">${dayCount}</p>
                            <p class="income">${income}</p>
                            <p class="expense">${expense}</p>
                            <form action="/showRecord" method="get" class="btnn btnn-arrow-right">
                                <input type="hidden" name="date" value="${year}-${String(month).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}">
                                    <button type="submit"/>
                                </input>
                            </form>
                        </td>`;
                }else{
                    calendarHtml +=
                        `<td class="calendar_td" data-date="${year}-${String(month).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}">
                            <p class="emptyDay">${dayCount}</p>
                        </td>`;
                }
                dayCount++;
            }
        }
        calendarHtml += '</tr>';
    }
    calendarHtml += '</table>';

    return calendarHtml;
}

showCalendar(year, month);