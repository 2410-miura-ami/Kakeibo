// 収支、大カテゴリ、小カテゴリの選択肢を配列でそれぞれ用意
//収支
const categories = [
  {category: '収入', value: '1'},
  {category: '支出', value: '2'}
];

//大カテゴリ
const subCategories = [
  {category: '収入', name: '給与所得', value: '1'},
  {category: '収入', name: 'その他（収入）', value: '2'},
  {category: '支出', name: '食費', value: '3'},
  {category: '支出', name: '日用品', value: '4'},
  {category: '支出', name: '交通費', value: '5'},
  {category: '支出', name: '交際費', value: '6'},
  {category: '支出', name: '水道光熱費', value: '7'},
  {category: '支出', name: '住宅', value: '8'},
  {category: '支出', name: 'その他（支出）', value: '9'}
];

//小カテゴリ
const products = [
  {subCategory: '給与所得', name: '月給', value: '1'},
  {subCategory: '給与所得', name: '賞与', value: '2'},
  {subCategory: 'その他（収入）', name: '副業', value: '3'},
  {subCategory: 'その他（収入）', name: '臨時収入', value: '4'},
  {subCategory: '食費', name: '食料品', value: '5'},
  {subCategory: '食費', name: '外食', value: '6'},
  {subCategory: '食費', name: 'その他', value: '7'},
  {subCategory: '日用品', name: '消耗品', value: '8'},
  {subCategory: '日用品', name: '子育て用品', value: '9'},
  {subCategory: '日用品', name: 'ペット用品', value: '10'},
  {subCategory: '日用品', name: 'その他', value: '11'},
  {subCategory: '交通費', name: '電車', value: '12'},
  {subCategory: '交通費', name: 'バス', value: '13'},
  {subCategory: '交通費', name: 'タクシー', value: '14'},
  {subCategory: '交通費', name: 'その他', value: '15'},
  {subCategory: '交際費', name: '飲み会', value: '16'},
  {subCategory: '交際費', name: 'プレゼント代', value: '17'},
  {subCategory: '交際費', name: '冠婚葬祭', value: '18'},
  {subCategory: '交際費', name: 'その他', value: '19'},
  {subCategory: '水道光熱費', name: '水道', value: '20'},
  {subCategory: '水道光熱費', name: '電気', value: '21'},
  {subCategory: '水道光熱費', name: 'ガス', value: '22'},
  {subCategory: '水道光熱費', name: 'その他', value: '23'},
  {subCategory: '住宅', name: '家賃', value: '24'},
  {subCategory: '住宅', name: '家具', value: '25'},
  {subCategory: '住宅', name: '家電', value: '26'},
  {subCategory: '住宅', name: '地震火災保険', value: '27'},
  {subCategory: '住宅', name: 'その他', value: '28'},
  {subCategory: 'その他（支出）', name: '未分類', value: '29'}
];

// 「選択してください」というプルダウンの選択肢を作成する関数
function firstOption() {
  const first = document.createElement('option');
  first.textContent = '選択してください';
  first.value = 0;

  return first;
}

// 引数に指定されたノードをすべて削除する関数
function optionClear(node) {
  const options = document.querySelectorAll(node);
  options.forEach(option => {
    option.remove();
  });
}

const categorySelect3 = document.getElementById('category-select-3');
const subCategorySelect3 = document.getElementById('sub-category-select-3');
const productSelect = document.getElementById('product-select');

//デフォルト値を既存の記録から設定
const defaultCategory = bop;
const defaultSubCategory = bigCategoryId;
const defaultProduct = smallCategoryId;
let defaultCategoryName = '';
let defaultSubCategoryName = '';

// 収支のプルダウンを生成
//繰り返し処理で<option>タグに配列categoriesの値を設定していく
categories.forEach(category => {
  //optionという変数に<option>タグをセット
  const option = document.createElement('option');
  //<option>タグにテキスト内容 (表示される文字)とvalue属性をセット
  option.textContent = category.category;
  option.value = category.value;
  //label要素は選択された収支と表示する大カテゴリの比較のために設定
  option.label = category.category;

　//デフォルト値と合致する場合にselected要素（初期表示用）を追加
  if (String(category.value) === String(defaultCategory)) {
      option.selected = true;
      //デフォルト値と合致するカテゴリ名を変数にセット
      defaultCategoryName = category.category;
  }
  //html上の<select>タグのid属性を識別子として、作成した<option>タグを追加
  categorySelect3.appendChild(option);
});


// 大カテゴリのプルダウンを生成
//大カテゴリのプルダウンの選択肢を全て削除し、選択（クリック）できるようにする
optionClear('#sub-category-select-3 > option');
subCategorySelect3.disabled = false;

/*①*/
//subCategorySelect3にfirstOption()関数で作った<option>タグを追加
subCategorySelect3.appendChild(firstOption());
//支出で選択されたカテゴリーと同じ大カテゴリのみを、プルダウンの選択肢に設定する
subCategories.forEach(subCategory => {

  //選択された<option>タグのvalue要素が1かつ繰り返し処理のsubCategory.categoryが収入のとき
  if (defaultCategoryName == subCategory.category) {
    //optionという変数に<option>タグをセット
    const option = document.createElement('option');
    //<option>タグにテキスト内容 (表示される文字)とvalue属性をセット
    option.textContent = subCategory.name;
    option.value = subCategory.value;
    //label要素は選択された大カテゴリと表示する小カテゴリの比較のために設定
    option.label = subCategory.name;
    //html上の<select>タグのid属性を識別子として、作成した<option>タグを追加
    subCategorySelect3.appendChild(option);

    //デフォルト値と合致する場合にselected要素（初期表示用）を追加
    if (subCategory.value == defaultSubCategory) {
      option.selected = true;
      //デフォルト値と合致するカテゴリ名を変数にセット
      defaultSubCategoryName = subCategory.name
    }
  }

  //支出が選択されていない（「選択してください」になっている）とき、大カテゴリを選択（クリック）できないようにする
  //選択してください」の<option>タグのvalue属性をhtml上で0に設定しているため、それをチェック
  if (categorySelect3.value == 0) {
    subCategorySelect3.disabled = true;
    return;
  }
});

//小カテゴリのプルダウンを生成
//小カテゴリのプルダウンの選択肢を全て削除し、選択（クリック）できるようにする
optionClear('#product-select > option');
productSelect.disabled = false;

/*①の小カテゴリバージョン*/
productSelect.appendChild(firstOption());

products.forEach(product => {
  if (defaultSubCategoryName == product.subCategory) {
    const option = document.createElement('option');
    option.textContent = product.name;
    option.value = product.value;

    if (product.value == defaultProduct) {
      option.selected = true;
    }

    productSelect.appendChild(option);
  }

  if (subCategorySelect3.value == 0) {
    productSelect.disabled = true;
    return;
  }
});
/* ↑ここから上はプルダウンのデフォルト値設定のための定義↑ */


/* ↓ここから下は通常のプルダウンの動きを定義↓ */
/*(１)*/
//支出が選択されたら大カテゴリのプルダウンを生成
categorySelect3.addEventListener('input', () => {

  //大カテゴリのプルダウンを「選択してください」のみにし、選択（クリック）できるようにする
  optionClear('#sub-category-select-3 > option');
  subCategorySelect3.appendChild(firstOption());
  subCategorySelect3.disabled = false;

  //小カテゴリのプルダウンを「選択してください」のみにし、選択（クリック）できないようにする
  optionClear('#product-select > option');
  productSelect.appendChild(firstOption());
  productSelect.disabled = true;

  //支出が選択されていない（「選択してください」になっている）とき、小分類を選択（クリック）できないようにする
  if (categorySelect3.value == 0) {
    subCategorySelect3.disabled = true;
    return;
  }

  //収支で選択されたカテゴリーと同じ大カテゴリのみを、プルダウンの選択肢に設定する
  subCategories.forEach(subCategory => {

      //selectedOptions[0].labelで選択された<option>タグのlabel要素を取得
      if (categorySelect3.selectedOptions[0].label == subCategory.category){
          const option = document.createElement('option');
          option.textContent = subCategory.name;
          option.value = subCategory.value;
          subCategorySelect3.appendChild(option);
      }
  });
});
/*(１)の小カテゴリバリデーション*/
//大カテゴリが選択されたら小カテゴリのプルダウンを生成
subCategorySelect3.addEventListener('input', () => {

  //小カテゴリのプルダウンを「選択してください」のみにし、選択（クリック）できるようにする
  optionClear('#product-select > option');
  productSelect.appendChild(firstOption());
  productSelect.disabled = false;

  //大カテゴリが選択されていない（「選択してください」になっている）とき、商品を選択（クリック）できないようにする
  if (subCategorySelect3.value == 0) {
    productSelect.disabled = true;
    return;
  }

  //大カテゴリで選択されたカテゴリーと同じ小カテゴリのみを、プルダウンの選択肢に設定する
  products.forEach(product => {
    if (subCategorySelect3.selectedOptions[0].label == product.subCategory) {
      const option = document.createElement('option');
      option.textContent = product.name;
      option.value = product.value;

      productSelect.appendChild(option);
    }
  });
});
