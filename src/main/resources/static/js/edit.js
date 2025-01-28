// 大分類、小分類、商品名の選択肢を配列でそれぞれ用意
const categories = [
  {category: '収入', value: '1'},
  {category: '支出', value: '2'}
];

// 小分類と商品名は、それぞれ一つ前の選択肢と紐付けるためにオブジェクト型を使う
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


// 大分類のプルダウンを生成
categories.forEach(category => {
  const option = document.createElement('option');
  option.textContent = category.category;

　//デフォルト値と合致する場合にselected要素を追加
  if (String(category.value) === String(defaultCategory)) {
      option.selected = true;
      //document.getElementById('hidden-category').value = defaultCategory;
    }

  categorySelect3.appendChild(option);
});


// 小分類のプルダウンの選択肢を全て削除し、選択（クリック）できるようにする
optionClear('#sub-category-select-3 > option');
subCategorySelect3.disabled = false;

// 大分類で選択されたカテゴリーと同じ小分類のみを、プルダウンの選択肢に設定する
subCategories.forEach(subCategory => {
  if (String(categorySelect3.value) === String(subCategory.category)) {
    const option = document.createElement('option');
    option.textContent = subCategory.name;

    //デフォルト値と合致する場合にselected要素を追加
    if (String(subCategory.value) === String(defaultSubCategory)) {
      option.selected = true;
      //document.getElementById('hidden-big-category').value = defaultSubCategory;
    }

  subCategorySelect3.appendChild(option);
  }
});

// 商品のプルダウンの選択肢を全て削除し、選択（クリック）できるようにする
optionClear('#product-select > option');
productSelect.disabled = false;

// 小分類で選択されたカテゴリーと同じ商品のみを、プルダウンの選択肢に設定する
products.forEach(product => {
  if (String(subCategorySelect3.value) === String(product.subCategory)) {
    const option = document.createElement('option');
    option.textContent = product.name;

    //デフォルト値と合致する場合にselected要素を追加
    if (String(product.value) === String(defaultProduct)) {
        option.selected = true;
        document.getElementById('hidden-small-category').value = defaultProduct;
    }

    productSelect.appendChild(option);
  }
});
/* ↑ここから上はプルダウンのデフォルト値設定のための定義↑ */

/* ↓ここから下は通常のプルダウンの動きを定義↓ */
// 大分類が選択されたら小分類のプルダウンを生成
categorySelect3.addEventListener('input', () => {
  const selectedCategory = categorySelect3.value;
  //document.getElementById('hidden-category').value = selectedCategory;

  // 小分類のプルダウンを「選択してください」のみにし、選択（クリック）できるようにする
  optionClear('#sub-category-select-3 > option');
  subCategorySelect3.appendChild(firstOption());
  subCategorySelect3.disabled = false;

  // 商品のプルダウンを「選択してください」のみにし、選択（クリック）できないようにする
  optionClear('#product-select > option');
  productSelect.appendChild(firstOption());
  productSelect.disabled = true;

  // 大分類が選択されていない（「選択してください」になっている）とき、小分類を選択（クリック）できないようにする
  if (String(categorySelect3.value) === '選択してください') {
    subCategorySelect3.disabled = true;
    return;
  }

  // 大分類で選択されたカテゴリーと同じ小分類のみを、プルダウンの選択肢に設定する
  subCategories.forEach(subCategory => {
    if (String(categorySelect3.value) === String(subCategory.category)) {
      const option = document.createElement('option');
      option.textContent = subCategory.name;

      subCategorySelect3.appendChild(option);
    }
  });
});

// 小分類が選択されたら商品のプルダウンを生成
subCategorySelect3.addEventListener('input', () => {
  const selectedSubCategory = subCategorySelect3.value;
  //document.getElementById('hidden-big-category').value = selectedSubCategory;

  // 商品のプルダウンを「選択してください」のみにし、選択（クリック）できるようにする
  optionClear('#product-select > option');
  productSelect.appendChild(firstOption());
  productSelect.disabled = false;

  // 小分類が選択されていない（「選択してください」になっている）とき、商品を選択（クリック）できないようにする
  if (String(subCategorySelect3.value) === '選択してください') {
    productSelect.disabled = true;
    return;
  }

  // 小分類で選択されたカテゴリーと同じ商品のみを、プルダウンの選択肢に設定する
  products.forEach(product => {
    if (String(subCategorySelect3.value) === String(product.subCategory)) {
      const option = document.createElement('option');
      option.textContent = product.name;
      option.value = product.value;

      productSelect.appendChild(option);
    }
  });
});

productSelect.addEventListener('input', () => {
  products.forEach(product => {
    if (String(productSelect.value) === String(product.value)) {
      document.getElementById('hidden-small-category').value = product.value;
    }
  });
});