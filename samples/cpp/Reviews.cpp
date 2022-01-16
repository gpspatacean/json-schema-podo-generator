#include "Reviews.hpp"

namespace podo_generator { 

int Reviews::GetRate() const {
    return m_rate;
}

void Reviews::SetRate(int value) {
    m_rate = value;
}

const std::string& Reviews::GetUser() const {
    return m_user;
}

void Reviews::SetUser(const std::string& value) {
    m_user = value;
}


bool Reviews::Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const {
    writer.StartObject();

    /**************** <m_rate> ****************/
    writer.String("rate");
    writer.Int(m_rate);
    /**************** </m_rate> ****************/

    /**************** <m_user> ****************/
    writer.String("user");
    writer.String(m_user.c_str());
    /**************** </m_user> ****************/

    writer.EndObject();
    return true;
}

bool Reviews::Deserialize(const rapidjson::Value& obj){
    m_rate = obj["rate"].GetInt();

    m_user = obj["user"].GetString();

    return true;
}

} //namespace podo_generator 
