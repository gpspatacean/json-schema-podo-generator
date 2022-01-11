#include "ModelBase.hpp"

std::string ModelBase::ToString() const{
    rapidjson::StringBuffer sb{};
    rapidjson::Writer<rapidjson::StringBuffer> writer(sb);
    Serialize(writer);
    writer.Flush();
    return sb.GetString();
}

bool ModelBase::FromString(const std::string& s){
    rapidjson::Document doc{};
    if(!InitDocument(s, doc))
        return false;

    return Deserialize(doc);
}

bool ModelBase::InitDocument(const std::string& s, rapidjson::Document& doc) {
    if( s.empty() )
        return false;

    std::string jsonString{s};
    return !doc.Parse(jsonString.c_str()).HasParseError();
}


void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, int i) {
    writer.Int(i);
}

void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, double d) {
    writer.Double(d);
}

void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, bool b) {
    writer.Bool(b);
}

void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, const std::string& s) {
    writer.String(s.c_str());
}

void Read(const rapidjson::Value& object, int& i) {
    i = object.GetInt();
}

void Read(const rapidjson::Value& object, double& d) {
    d = object.GetDouble();
}

void Read(const rapidjson::Value& object, bool& b) {
    b = object.GetBool();
}

void Read(const rapidjson::Value& object, std::string& s) {
    s = object.GetString();
}