package com.happypill.application.util;

import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitialiser implements ApplicationRunner {

    private final CategoryRepository categoryRepository;
    private final CategoryInfoRepository categoryInfoRepository;
    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Category> categoryList = Arrays.asList(
                Category.of(SnowflakeUtil.nextId(), "www.vitamin_thumbnail.com", "www.vitamin_banner.com"),
                Category.of(SnowflakeUtil.nextId(), "www.mineral_thumbnail.com", "www.mineral_banner.com"),
                Category.of(SnowflakeUtil.nextId(), "www.herb_thumbnail.com", "www.herb_banner.com"),
                Category.of(SnowflakeUtil.nextId(), "www.probiotics_thumbnail.com", "www.probiotics_banner.com")
        );

        List<CategoryInfo> categoryInfoList = Arrays.asList(
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민", "비타민은 신진대사와 면역 기능을 유지하는 데 꼭 필요한 필수 영양소입니다." +
                        "부족할 경우 피로, 면역력 저하, 피부 트러블 등 다양한 건강 문제가 발생할 수 있습니다.",  categoryList.get(0)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "vitamin", "Vitamins are chemical compounds that are needed in small amounts " +
                        "for the human body to work correctly.",  categoryList.get(0)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "미네랄", "미네랄은 뼈 건강, 신경 전달, 체내 수분 균형 등 다양한 생리 기능에 필수적인 영양소입니다." +
                        "불균형하거나 부족하면 피로, 근육 경련, 면역력 저하 등이 나타날 수 있습니다.",  categoryList.get(1)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "mineral", "Minerals are chemical elements required as an essential nutrient " +
                        "by our body to perform essential functions. ",  categoryList.get(1)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "허브", "허브는 자연에서 유래한 식물 성분으로, 면역 강화, 소화 개선, 스트레스 완화 등에 도움을 줍니다." +
                        "오랜 전통과 현대 과학이 입증한 천연 건강 보조 원료로 널리 사용됩니다.",  categoryList.get(2)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "herb", "Herbal supplements are made from plants and used for their therapeutic properties.",  categoryList.get(2)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "유산균", "유산균은 장내 유익균을 늘려 소화 기능을 개선하고 면역력을 높여줍니다." +
                        "장 건강은 곧 전신 건강으로 이어지며, 꾸준한 유산균 섭취가 중요합니다.",  categoryList.get(3)),
                CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "probiotics", "Probiotics are live bacteria and yeasts that support your digestive system health. " +
                        "We select strains that have been thoroughly studied for their benefits.",  categoryList.get(3))
        );
        // 10 비타민, 6 미네랄, 4 허브, 유산균 2

        List<Product> productList = Arrays.asList(
                Product.of(SnowflakeUtil.nextId(), 101, 1001, "www.first_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 102, 1002, "www.second_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 103, 1003, "www.third_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 104, 1004, "www.fourth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 105, 1005, "www.fifth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 106, 1006, "www.sixth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 107, 1007, "www.seventh_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 108, 1008, "www.eighth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 109, 1009, "www.ninth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 110, 1100, "www.tenth_vitamin_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 201, 2001, "www.first_mineral_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 202, 2002, "www.second_mineral_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 203, 2003, "www.third_mineral_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 204, 2004, "www.fourth_mineral_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 205, 2005, "www.fifth_mineral_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 206, 2006, "www.sixth_mineral_Thumbnail.com", categoryList.get(0)),

                Product.of(SnowflakeUtil.nextId(), 1, 1100, "www.first_herb_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 2, 1200, "www.second_herb_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 3, 1300, "www.third_herb_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 4, 1400, "www.fourth_herb_Thumbnail.com", categoryList.get(0)),

                Product.of(SnowflakeUtil.nextId(), 0, 1000, "www.first_probiotics_Thumbnail.com", categoryList.get(0)),
                Product.of(SnowflakeUtil.nextId(), 0, 1000, "www.second_probiotics_Thumbnail.com", categoryList.get(0))
        );

        List<ProductInfo> productInfoList = Arrays.asList(

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 k2", "30 캡슐", "밥과 함께 드시오", "물과 함께 섭취하시오. 하루에 3개.", "Content image",
                        "비타민 K2는 심장과 뼈 건강에 필수적입니다. 비타민 K2는 칼슘이 동맥에 쌓이는 것을 방지해 심혈관 건강을 촉진하는 동시에, 칼슘이 뼈로 제대로 전달되도록 도와 뼈의 밀도와 강도를 높여줍니다. " +
                                "올바른 칼슘 이용을 통해 비타민 K2는 심장과 뼈 건강을 동시에 지원하며 전반적인 웰빙에 기여합니다.",
                        "삼성제약", "K2VITAL™ 형태의 프리미엄 비타민 K2(메나퀴논-7)로 구성되어 뼈 건강과 심장 건강을 지원합니다.", productList.get(0)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin K2", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Vitamin K2 is essential for heart and bone health. It helps prevent calcium from accumulating in the arteries, promoting cardiovascular health, while also directing calcium to the bones, " +
                                "enhancing bone density and strength. By ensuring proper calcium utilization, vitamin K2 supports both heart and bone health, contributing to overall well-being.",
                        "Samsung chemist", "Formulated with premium Vitamin K2 (menaquinone 7) as K2VITAL™ to support bone strength and heart Health", productList.get(0)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 D3", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 비건 비타민 D는 체내 흡수율이 뛰어난 콜레칼시페롤 형태로 제공됩니다. 비타민 D는 튼튼하고 건강한 뼈를 지원하며, 건강한 면역 체계 유지에 도움을 줍니다.",
                        "삼성제약", "칼슘 흡수를 도와주며 뼈 건강과 면역 건강을 함께 지원합니다", productList.get(1)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin D3", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Vegan Vitamin D is in the form of cholecalciferol for optimal absorption in the body. Vitamin D supports strong and healthy bones and helps our bodies maintain a healthy immune system.",
                        "Samsung chemist", "Helps calcium absorption and also supports bone health and immune health.", productList.get(1)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 B12", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 비타민 B12는 체내 최적 기능에 필수적인 천연 형태인 메틸코발라민으로 구성되어 있습니다. 1회 분량의 본 제품은 에너지 생성과 신경계 건강, 두뇌 기능을 지원합니다.",
                        "삼성제약", "에너지 생성에 도움을 주고 신경 기능을 지원합니다.", productList.get(2)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin B12", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Vitamin B12 is in the form of methylcobalamin, a naturally occurring form of B12 that is essential for optimal body function. " +
                                "Our single-dose formula supports energy production, nervous system health and brain function.",
                        "Samsung chemist", "Supports energy production and supports nervous function.", productList.get(2)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 D", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 비타민 D는 체내 흡수율이 뛰어난 콜레칼시페롤 형태로 제공됩니다. 비타민 D는 튼튼하고 건강한 뼈를 지원하며, 건강한 면역 체계 유지에 도움을 줍니다.",
                        "삼성제약", "칼슘 흡수를 도와주며 뼈 건강과 면역 건강을 함께 지원합니다.", productList.get(3)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin D", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Vitamin D is in the form of cholecalciferol for optimal absorption in the body. Vitamin D supports strong and healthy bones and helps our bodies maintain a healthy immune system.",
                        "Samsung chemist", "Helps calcium absorption and also supports bone health and immune health.", productList.get(3)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "종합 비타민 B", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 액티베이티드 B 콤플렉스는 7가지 필수 B군 비타민을 뛰어난 활성형태로 함유하고 있습니다. 각 B 비타민은 최적의 신체 기능과 건강을 위해 에너지 생성에 도움을 주고, 건강한 면역 체계를 지원하며, 신경계 건강을 유지하고, " +
                                "체내 자연 간 해독 과정을 돕는 역할을 합니다.",
                        "삼성제약", "에너지 수준을 지원하고 음식의 에너지 전환을 돕습니다.", productList.get(4)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Activated B Complex", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Activated B Complex contains 7 essential B group vitamins in their superior, activated form. Each B vitamin plays a role to ensure optimum body function and health: they aid energy production, " +
                                "support a healthy immune system, maintain your nervous system health and support natural liver detoxification processes in the body",
                        "Samsung chemist", "Supports energy levels and helps convert food into energy.", productList.get(4)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C 플러스", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 비타민 C 플러스는 항산화가 풍부한 로즈힙 추출물과 함께 조합되어 있습니다. 비타민 C 플러스는 건강한 면역 기능, 에너지 수준, 콜라겐 생성 및 두뇌 기능을 지원합니다.",
                        "삼성제약", "로즈힙과 함께하여 콜라겐 형성과 건강한 면역 체계를 지원합니다.", productList.get(5)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C Plus", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Vitamin C Plus is paired with Rosehip extract for an antioxidant rich blend. Vitamin C Plus helps to support a healthy immune system function, energy levels, collagen formation and brain function.",
                        "Samsung chemist", "Paired with Rosehip to support collagen formation and a healthy immune system.", productList.get(5)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "바이오틴", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "B 비타민 군에 속하는 비오틴은 모발과 피부 건강에 기여합니다. 비타민 B7로도 알려져 있으며, 신체 내 다양한 중요한 과정에서 필수적인 역할을 합니다.",
                        "삼성제약", "모발과 손톱 강화를 돕고 피부 건강을 지원합니다.", productList.get(6)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Biotin", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Part of the B vitamin family, Biotin contributes to hair and skin health. Also known as vitamin B7, it plays an essential role in a number of processes in the body.",
                        "Samsung chemist", "Supports hair and nail strength as well as skin health.", productList.get(6)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "강력한 항산화제로 면역력 강화와 피부 건강에 도움을 줍니다.",
                        "삼성제약", "비타민 C는 강력한 항산화 작용을 통해 활성산소로부터 세포를 보호하며, 면역 기능을 강화합니다. 또한 콜라겐 생성에 필수적이어서 피부 탄력과 상처 치유를 돕고, 철분 흡수를 촉진하여 전반적인 건강 유지에 기여합니다.", productList.get(7)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Vitamin C acts as a strong antioxidant, protecting cells from free radicals and enhancing immune function. It is also essential for collagen production, " +
                                "aiding skin elasticity and wound healing, and promotes iron absorption, contributing to overall health maintenance.",
                        "Samsung chemist", "A powerful antioxidant that helps boost immunity and supports healthy skin.", productList.get(7)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 D", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "비타민 D는 칼슘과 인의 흡수를 도와 뼈를 튼튼하게 유지하며, 면역 세포의 기능을 조절해 감염과 염증에 대한 방어력을 높입니다. 햇빛을 통해 합성되지만 부족할 경우 보충이 필요하며, 전반적인 신체 건강에 필수적입니다.",
                        "삼성제약", "뼈 건강과 면역 체계 유지에 중요한 역할을 합니다.", productList.get(8)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin D", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Vitamin D helps the absorption of calcium and phosphorus to keep bones strong, and regulates immune cell functions to enhance defense against infections and inflammation. " +
                                "It is synthesized through sunlight exposure, but supplementation may be necessary if deficient, making it essential for overall health.",
                        "Samsung chemist", "Plays an important role in maintaining bone health and the immune system.", productList.get(8)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 B12", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "비타민 B12는 신경 세포의 건강을 유지하고, 적혈구 생성에 관여해 피로 회복과 정신 집중에 도움을 줍니다. 또한 DNA 합성에도 필수적이며, 주로 동물성 식품에 존재해 채식주의자는 보충이 필요할 수 있습니다",
                        "삼성제약", "신경 건강과 에너지 생산에 필수적인 비타민입니다.", productList.get(9)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin B12", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Vitamin B12 maintains the health of nerve cells and is involved in red blood cell formation, helping with fatigue recovery and mental focus. " +
                                "It is also crucial for DNA synthesis and is primarily found in animal products, so supplementation may be needed for vegetarians.",
                        "Samsung chemist", "An essential vitamin for nerve health and energy production.", productList.get(9)),



                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "마그네슘", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "마그네슘은 근육 이완, 신경계 건강과 두뇌 기능, 에너지 수준 및 심장 건강을 지원하는 필수 미네랄입니다. 저희 마그네슘은 생체 이용률이 높아 체내에서 쉽게 흡수되고 활용됩니다.",
                        "삼성제약", "에너지 생성과 근육 이완을 돕습니다.", productList.get(10)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Magnesium", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Magnesium is an essential mineral that supports muscle relaxation, nervous system health and brain function, " +
                                "energy levels and heart health. Our form of Magnesium is highly bioavailable, meaning your body can absorb and use it easily.",
                        "Samsung chemist", "Supports energy production and muscle relaxation.", productList.get(10)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "아연", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "아연은 모든 세포에서 중요한 역할을 하는 필수 미량 미네랄입니다. 저희 아연은 생체이용률이 높은 글리시네이트 형태로 제공되며, 최적의 미네랄 균형을 위해 구리와 함께 배합되어 있습니다. " +
                                "이 1회 분량 포뮬러는 건강한 면역 체계 지원과 피부 회복 촉진에 핵심적인 역할을 합니다.",
                        "삼성제약", "건강한 면역 기능과 피부 회복을 돕습니다.", productList.get(11)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Zinc", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Zinc is an essential trace mineral playing a role in every cell. Our Zinc is in the form of zinc glycinate, a highly bioavailable form, and coupled with copper for optimal mineral balance. " +
                                "This single-dose formula is key in supporting a healthy immune system, and to promote skin repair.",
                        "Samsung chemist", "Helps support a healthy immune system function and skin repair.", productList.get(11)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "크롬 맥스", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "크롬은 혈당 수치 조절에 중요한 역할을 하는 필수 미네랄입니다. 크롬은 세포 내 포도당 흡수와 대사를 유지하는 데 도움을 줍니다.",
                        "삼성제약", "고함량 보충제로 250마이크로그램의 원소 크롬을 제공하여 포도당 대사를 돕습니다.", productList.get(12)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Chromium MAX", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Chromium is an essential mineral that plays a role in how the body regulate blood sugar levels. Chromium helps to maintain cellular uptake and metabolism of glucose.",
                        "Samsung chemist", "High-strength supplement providing 250 micrograms of elemental chromium to assist glucose metabolism", productList.get(12)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "칼슘 플러스 D3", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 프리미엄 칼슘 플러스 포뮬러는 식물성 비타민 D가 강화되어 체내 칼슘 흡수를 높여줍니다. 두 성분이 함께 뼈의 강도와 회복은 물론 근육 기능까지 지원합니다.",
                        "삼성제약", "식물성 비타민 D3를 특별히 강화하여 뼈 건강을 지원합니다.", productList.get(13)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Calcium Plus D3", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our premium Calcium Plus formula is enriched with a plant-based Vitamin D to increase calcium absorption in the body. The pair work together to support bone strength and repair, plus muscle function.",
                        "Samsung chemist", "Specially enriched with plant-based Vitamin D3 to support bone health.", productList.get(13)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 철분 포뮬러는 비타민 C와 함께 배합되어 흡수를 촉진하고 체내 생성되는 활성산소를 감소시켜줍니다. 위에 자극이 적으며, 에너지 생성, 두뇌 건강 및 면역 체계 지원에 도움을 줍니다.",
                        "삼성제약", "저희 철분 제품은 비타민 C와 결합하여 에너지 생성에 도움을 줍니다.", productList.get(14)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Iron", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Iron formula is paired with Vitamin C to enhance absorption and reduce free radicals formed in the body. It is gentle on the stomach and helps support energy production, brain health and immune system health.",
                        "Samsung chemist", "Our Iron is combined with Vitamin C to support energy production.", productList.get(14)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "셀레늄", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "셀레늄은 체내에서 항산화 효소의 주요 구성 요소로 작용하여 활성산소로부터 세포를 보호하고, 면역력 강화와 갑상선 기능 유지에 중요한 역할을 합니다. 또한 심장 건강과 염증 감소에도 기여합니다.",
                        "삼성제약", "강력한 항산화 작용으로 세포 보호에 도움을 주는 미네랄입니다.", productList.get(15)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Selenium", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Selenium acts as a key component of antioxidant enzymes in the body, protecting cells from free radical damage. " +
                                "It plays an important role in boosting immunity, maintaining thyroid function, and contributes to heart health and inflammation reduction.",
                        "Samsung chemist", "A mineral that helps protect cells through powerful antioxidant action.", productList.get(15)),



                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "크랜베리", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 크랜베리 제품은 항산화 성분이 풍부하며, 피부 건강을 돕고 요로 건강을 유지하는 데 도움을 주는 비타민 C와 실리카를 함께 배합하였습니다. 크랜베리는 전통적으로 건강한 요로를 지원하는 데 사용되어 왔으며, " +
                                "저희의 고농축 고함량 포뮬라는 많은 양의 열매를 섭취하지 않고도 크랜베리의 치료 효과를 누릴 수 있도록 설계되었습니다.",
                        "삼성제약", "항산화제는 전통적으로 건강한 요로를 유지하는 데 도움을 주기 위해 사용되었습니다.", productList.get(16)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Cranberry", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Cranberry is antioxidant rich and formulated with Vitamin C and Silica to support skin health and maintain urinary tract health. Cranberries have been traditionally used to support a healthy urinary tract and our concentrated, " +
                                "high dose formula supports the therapeutic benefits of cranberry without a large intake of the berry itself.",
                        "Samsung chemist", "Antioxidant traditionally used to help maintain a healthy urinary tract.", productList.get(16)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "밀크시슬", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 밀크시슬 제품은 표준화된 밀크시슬 추출물을 함유하여 간 건강과 소화 건강을 지원합니다.",
                        "삼성제약", "소화 건강과 간 건강을 지원하는 허브 추출물.", productList.get(17)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "St Mary's Thistle", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our St Mary’s Thistle 35 000 contains a standardised extract of St Mary’s thistle to support healthy liver function and digestive health.",
                        "Samsung chemist", "Herbal extract that supports digestive health and liver health.", productList.get(17)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "은행과 브라미", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 강력한 은행나무(Ginkgo)와 브라미(Brahmi) 조합은 기억력과 학습 능력을 지원하며 매일 뇌 기능을 돕는 효과적인 포뮬러입니다. 저희는 프리미엄 특허 추출물인 바코마인드를 사용하였으며, " +
                                "은행나무와 함께 전통적으로 뇌를 영양하고 인지 기능을 지원하며 기억력을 증진하는 데 활용되어 왔습니다.",
                        "삼성제약", "기억력과 정신 집중을 지원하기 위해 전통적으로 사용된 허브 혼합물입니다.", productList.get(18)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Ginkgo and Brahmi", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our potent combination of Ginkgo and Brahmi is a powerful formula to support memory, learning and support brain function daily. " +
                                "We used the premium and patented extract BacoMind which together with Ginkgo have been traditionally used to nourish the brain, support cognition and promote memory.",
                        "Samsung chemist", "Herbal blend traditionally used to support memory and support mental concentration and focus.", productList.get(18)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "활성화 강황", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "커큐민은 강황의 주요 활성 성분으로, 체내에서 항염증 및 항산화 효과를 발휘합니다. 저희 활성화 커큐민 포뮬러에는 흡수를 돕기 위해 흑후추가 함유되어 있습니다. " +
                                "이 프리미엄 포뮬러는 관절 통증, 통증 완화 및 부기 감소를 지원하며 건강한 소화기 기능을 유지하는 데 도움을 줍니다.",
                        "삼성제약", "경미한 관절 염증과 부기를 완화하는 데 사용되는 아유르베다 허브입니다.", productList.get(19)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Activated Curcumin", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Curcumin is the active constituent in turmeric and exerts anti-inflammatory and antioxidant effects in the body. Our Activated Curcumin formula contains black pepper to enhance absorption. " +
                                "This premium formula supports joint pain, aches and swelling and maintains a healthy digestive system function.",
                        "Samsung chemist", "Ayurvedic herb used to decrease mild joint inflammation and swelling.", productList.get(19)),


                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "매일 유산균", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 데일리 프로바이오틱스는 장 건강에 좋은 3가지 균주를 프리미엄으로 혼합하여 체내 소화기 기능을 지원하고 건강한 면역 체계를 유지하는 데 도움을 줍니다. " +
                                "이 혼합물은 배변 활동을 원활하게 하고, 복부 팽만감, 가스, 방귀 등의 소화 불편 증상을 관리하도록 설계되었습니다.",
                        "삼성제약", "좋은 장내 세균을 회복시키고 건강한 면역 기능을 지원합니다.", productList.get(20)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Daily Probiotic", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Daily Probiotics is a premium blend of 3 strains of gut friendly bacteria to help support the body’s digestive system function and maintain a healthy immune system. " +
                                "This blend has been formulated to support bowel regularity and manage digestive symptoms including bloating, gas and flatulence.",
                        "Samsung chemist", "Helps restore good gut flora and supports healthy immune system function.", productList.get(20)),

                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "유산균 SB", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image",
                        "저희 프로바이오틱스 SB는 항생제 복용 중 및 복용 후에도 체내 소화기 기능을 지원하도록 특별히 개발된 장 친화적 3가지 균주 프리미엄 혼합 제품입니다. " +
                                "이 혼합물은 배변 활동을 원활하게 하고 복부 팽만감, 가스, 방귀 등 소화 불편 증상을 관리하는 데 도움을 줍니다.",
                        "삼성제약", "항생제 복용 중 및 복용 후 좋은 장내 세균을 회복시키고 면역 체계를 지원합니다.", productList.get(21)),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Probiotics SB", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image",
                        "Our Probiotics SB is a premium blend of 3 strains of gut friendly bacteria that has been specially formulated to help support the body’s digestive system function during and after antibiotics use. " +
                                "This blend has been formulated to support bowel regularity and manage digestive symptoms including bloating, gas and flatulence.",
                        "Samsung chemist", "Helps restore good gut flora during and after antibiotics use, and supports the immune system.", productList.get(21))
                );

        // 10 비타민, 6 미네랄, 4 허브, 유산균 2
        List<ProductPriceHistory> productPriceHistoryList = Arrays.asList(
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 8000, false, productList.get(0)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 16000, true, productList.get(0)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 11000, true, productList.get(1)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 9000, true, productList.get(2)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 9000, true, productList.get(3)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 14000, true, productList.get(4)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 12000, true, productList.get(5)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 9000, true, productList.get(6)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 6000, true, productList.get(7)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 5000, true, productList.get(8)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 12000, true, productList.get(9)),

                ProductPriceHistory.of(SnowflakeUtil.nextId(), 11000, true, productList.get(10)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 11000, true, productList.get(11)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 12000, true, productList.get(12)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 14000, true, productList.get(13)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 14000, true, productList.get(14)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 10000, true, productList.get(15)),

                ProductPriceHistory.of(SnowflakeUtil.nextId(), 18000, true, productList.get(16)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 18000, true, productList.get(17)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 19000, true, productList.get(18)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 16000, true, productList.get(19)),

                ProductPriceHistory.of(SnowflakeUtil.nextId(), 15000, true, productList.get(20)),
                ProductPriceHistory.of(SnowflakeUtil.nextId(), 19000, true, productList.get(21))
        );

        categoryRepository.saveAll(categoryList);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.saveAll(productList);
        productInfoRepository.saveAll(productInfoList);
        productPriceRepository.saveAll(productPriceHistoryList);
    }
}