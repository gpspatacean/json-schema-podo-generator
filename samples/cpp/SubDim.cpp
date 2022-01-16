#include "SubDim.hpp"

namespace podo_generator { 

double SubDim::GetWeight() const {
    return m_weight;
}

void SubDim::SetWeight(double value) {
    m_weight = value;
}

double SubDim::GetDensity() const {
    return m_density;
}

void SubDim::SetDensity(double value) {
    m_density = value;
}


bool SubDim::Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const {
    writer.StartObject();

    /**************** <m_weight> ****************/
    writer.String("weight");
    writer.Double(m_weight);
    /**************** </m_weight> ****************/

    /**************** <m_density> ****************/
    writer.String("density");
    writer.Double(m_density);
    /**************** </m_density> ****************/

    writer.EndObject();
    return true;
}

bool SubDim::Deserialize(const rapidjson::Value& obj){
    m_weight = obj["weight"].GetDouble();

    m_density = obj["density"].GetDouble();

    return true;
}

} //namespace podo_generator 
