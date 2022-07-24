#include "Product.hpp"

namespace podo_generator { 

int Product::GetProductId() const {
    return m_productId;
}

void Product::SetProductId(int value) {
    m_productId = value;
}

const std::string& Product::GetProductName() const {
    return m_productName;
}

void Product::SetProductName(const std::string& value) {
    m_productName = value;
}

double Product::GetPrice() const {
    return m_price;
}

void Product::SetPrice(double value) {
    m_price = value;
}

const std::vector<std::string>& Product::GetTags() const {
    return m_tags;
}

void Product::SetTags(const std::vector<std::string>& value) {
    m_tags = value;
}

const std::vector<Review>& Product::GetReviews() const {
    return m_reviews;
}

void Product::SetReviews(const std::vector<Review>& value) {
    m_reviews = value;
}

const Dimensions& Product::GetDimensions() const {
    return m_dimensions;
}

void Product::SetDimensions(const Dimensions& value) {
    m_dimensions = value;
}


bool Product::Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const {
    writer.StartObject();

    /**************** <m_productId> ****************/
    writer.String("productId");
    writer.Int(m_productId);
    /**************** </m_productId> ****************/

    /**************** <m_productName> ****************/
    writer.String("productName");
    writer.String(m_productName.c_str());
    /**************** </m_productName> ****************/

    /**************** <m_price> ****************/
    writer.String("price");
    writer.Double(m_price);
    /**************** </m_price> ****************/

    /**************** <m_tags> ****************/
    writer.String("tags");
    writer.StartArray();
    for( const auto& element : m_tags ) {
        Write(writer, element);
    }
    writer.EndArray();
    /**************** </m_tags> ****************/

    /**************** <m_reviews> ****************/
    writer.String("reviews");
    writer.StartArray();
    for( const auto& element : m_reviews ) {
        element.Serialize(writer);
    }
    writer.EndArray();
    /**************** </m_reviews> ****************/

    /**************** <m_dimensions> ****************/
    writer.String("dimensions");
    m_dimensions.Serialize(writer);
    /**************** </m_dimensions> ****************/

    writer.EndObject();
    return true;
}

bool Product::Deserialize(const rapidjson::Value& obj){
    m_productId = obj["productId"].GetInt();

    m_productName = obj["productName"].GetString();

    m_price = obj["price"].GetDouble();

    for( const auto& v : obj["tags"].GetArray() ) {
        decltype (m_tags)::value_type innerElement{};
        Read(v, innerElement);
        m_tags.push_back(innerElement);
    }

    for( const auto& v : obj["reviews"].GetArray() ) {
        decltype (m_reviews)::value_type innerElement{};
        innerElement.Deserialize(v);
        m_reviews.push_back(innerElement);
    }

    m_dimensions.Deserialize(obj["dimensions"]);

    return true;
}

} //namespace podo_generator 
