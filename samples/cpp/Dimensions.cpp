#include "Dimensions.hpp"

namespace podo_generator { 

double Dimensions::GetLength() const {
    return m_length;
}

void Dimensions::SetLength(double value) {
    m_length = value;
}

double Dimensions::GetWidth() const {
    return m_width;
}

void Dimensions::SetWidth(double value) {
    m_width = value;
}

double Dimensions::GetHeight() const {
    return m_height;
}

void Dimensions::SetHeight(double value) {
    m_height = value;
}

const SubDim& Dimensions::GetSubDim() const {
    return m_subDim;
}

void Dimensions::SetSubDim(const SubDim& value) {
    m_subDim = value;
}


bool Dimensions::Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const {
    writer.StartObject();

    /**************** <m_length> ****************/
    writer.String("length");
    writer.Double(m_length);
    /**************** </m_length> ****************/

    /**************** <m_width> ****************/
    writer.String("width");
    writer.Double(m_width);
    /**************** </m_width> ****************/

    /**************** <m_height> ****************/
    writer.String("height");
    writer.Double(m_height);
    /**************** </m_height> ****************/

    /**************** <m_subDim> ****************/
    writer.String("subDim");
    m_subDim.Serialize(writer);
    /**************** </m_subDim> ****************/

    writer.EndObject();
    return true;
}

bool Dimensions::Deserialize(const rapidjson::Value& obj){
    m_length = obj["length"].GetDouble();

    m_width = obj["width"].GetDouble();

    m_height = obj["height"].GetDouble();

    m_subDim.Deserialize(obj["subDim"]);

    return true;
}

} //namespace podo_generator 
